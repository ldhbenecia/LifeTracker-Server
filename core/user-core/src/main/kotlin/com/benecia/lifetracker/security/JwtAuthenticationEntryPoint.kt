package com.benecia.lifetracker.security

import com.benecia.lifetracker.common.event.ErrorOccuredEvent
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint(
    private val publisher: ApplicationEventPublisher,
) : AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)
    private val objectMapper = ObjectMapper()

    // 공격 스캔 패턴들 - 이런 요청들은 로그와 이벤트를 발생시키지 않음
    private val attackPatterns = setOf(
        // 스크립트 파일들
        "debug.sh", "logs.sh", "test.sh", "backup.sh", "install.sh", "setup.sh",
        "index.php", "config.php", "admin.php", "login.php", "test.php", "wp-config.php",
        "eval-stdin.php", "xmlrpc.php",

        // 관리자/시스템 경로들
        "/admin", "/administrator", "/wp-admin", "/panel", "/manage", "/console",
        "/phpmyadmin", "/mysql", "/database", "/backup", "/test", "/tests", "/demo",
        "/debug", "/dev", "/development", "/old", "/tmp",

        // 설정/보안 파일들
        ".env", ".git", ".htaccess", ".htpasswd", "web.config", ".well-known",
        "security.txt", ".gitignore", ".gitconfig",

        // CMS 관련
        "/cms", "/drupal", "/joomla", "/wordpress", "/wp-content", "/wp-includes",

        // 서버 정보
        "server-info", "server-status", "info.php", "/cgi-bin",

        // 기타 공격 경로들
        "/vendor/phpunit", "/phpunit", "/geoserver", "/webui", "/solr",
        "/elasticsearch", "containers/json", "/actuator"
    )

    // 의심스러운 User-Agent들
    private val suspiciousUserAgents = setOf(
        "nmap", "sqlmap", "nikto", "masscan", "zgrab", "censys", "shodan",
        "scanner", "python-requests", "curl", "wget", "java/", "bot", "crawler"
    )

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException?,
    ) {
        val uri = request.requestURI
        val userAgent = request.getHeader("User-Agent") ?: ""
        val method = request.method
        val remoteAddr = getClientIpAddress(request)
        val isAttackPattern = isAttackAttempt(uri, userAgent)

        if (!isAttackPattern) {
            log.warn(
                "UNAUTHORIZED request: method={}, uri={}, query={}, remoteAddr={}, userAgent={}, authHeader={}, exception={}",
                method,
                uri,
                request.queryString,
                remoteAddr,
                userAgent,
                request.getHeader("Authorization"),
                authException?.message,
            )

            if (authException != null) {
                publisher.publishEvent(ErrorOccuredEvent(authException))
            }
        } else {
            log.debug(
                "BLOCKED attack attempt: method={}, uri={}, remoteAddr={}, userAgent={}",
                method, uri, remoteAddr, userAgent
            )
        }

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json;charset=UTF-8"

        val body = mapOf(
            "error" to "UNAUTHORIZED",
            "message" to "인증이 필요합니다. 올바른 토큰을 포함해 요청해 주세요.",
        )

        response.writer.use {
            it.write(objectMapper.writeValueAsString(body))
            it.flush()
        }
    }

    private fun isAttackAttempt(uri: String, userAgent: String): Boolean {
        // URI 패턴 체크
        val hasAttackPattern = attackPatterns.any { pattern ->
            uri.contains(pattern, ignoreCase = true)
        }

        // User-Agent 체크
        val hasSuspiciousAgent = suspiciousUserAgents.any { pattern ->
            userAgent.contains(pattern, ignoreCase = true)
        }

        // 위험한 파일 확장자 체크
        val hasDangerousExtension = listOf(
            ".sh", ".py", ".pl", ".rb", ".cgi", ".asp", ".aspx", ".jsp", ".php",
            ".zip", ".tar.gz", ".rar", ".bak", ".sql", ".old", ".orig", ".tmp",
            ".log", ".conf", ".config", ".ini", ".key", ".pem", ".crt"
        ).any { ext -> uri.endsWith(ext, ignoreCase = true) }

        return hasAttackPattern || hasSuspiciousAgent || hasDangerousExtension
    }

    private fun getClientIpAddress(request: HttpServletRequest): String {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        if (!xForwardedFor.isNullOrBlank()) {
            return xForwardedFor.split(",")[0].trim()
        }

        val xRealIp = request.getHeader("X-Real-IP")
        if (!xRealIp.isNullOrBlank()) {
            return xRealIp
        }

        return request.remoteAddr
    }
}
