package com.benecia.lifetracker.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {

    private val objectMapper = ObjectMapper()

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException?,
    ) {
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
}
