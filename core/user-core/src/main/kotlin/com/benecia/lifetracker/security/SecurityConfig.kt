package com.benecia.lifetracker.security

import com.benecia.lifetracker.security.filter.JwtAuthenticationFilter
import com.benecia.lifetracker.security.handler.OAuth2AuthenticationFailureHandler
import com.benecia.lifetracker.security.handler.OAuth2AuthenticationSuccessHandler
import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/public/**").permitAll()
                    .requestMatchers("/login/oauth2/**").permitAll()
                    .requestMatchers("/health").permitAll()
                    .requestMatchers("/docs/**").permitAll()
                    .requestMatchers("/actuator/prometheus").permitAll()
                    .requestMatchers("/actuator/health").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/favicon.ico").permitAll()
                    .requestMatchers("/robots.txt").permitAll()
                    .requestMatchers("/sitemap.xml").permitAll()
                    // === 보안 위험 경로들 명시적 차단 ===
                    // Git 관련
                    .requestMatchers("/.git/**").denyAll()
                    .requestMatchers("/.gitignore").denyAll()
                    .requestMatchers("/.gitconfig").denyAll()
                    // 환경설정 파일들
                    .requestMatchers("/.env*").denyAll()
                    .requestMatchers("/.htaccess").denyAll()
                    .requestMatchers("/.htpasswd").denyAll()
                    .requestMatchers("/web.config").denyAll()
                    // 보안 관련 경로
                    .requestMatchers("/.well-known/**").denyAll()
                    .requestMatchers("/security.txt").denyAll()
                    // PHP 관련 취약점 경로들
                    .requestMatchers("/vendor/**").denyAll()
                    .requestMatchers("/phpunit/**").denyAll()
                    .requestMatchers("/lib/phpunit/**").denyAll()
                    .requestMatchers("/**/phpunit/**").denyAll()
                    .requestMatchers("/**/eval-stdin.php").denyAll()
                    .requestMatchers("/index.php").denyAll()
                    .requestMatchers("/**/*.php").denyAll()
                    // 일반적인 공격 경로들
                    .requestMatchers("/admin/**").denyAll()
                    .requestMatchers("/administrator/**").denyAll()
                    .requestMatchers("/wp-admin/**").denyAll()
                    .requestMatchers("/wp-content/**").denyAll()
                    .requestMatchers("/wp-includes/**").denyAll()
                    .requestMatchers("/xmlrpc.php").denyAll()
                    // 개발/테스트 관련 경로
                    .requestMatchers("/test/**").denyAll()
                    .requestMatchers("/tests/**").denyAll()
                    .requestMatchers("/testing/**").denyAll()
                    .requestMatchers("/demo/**").denyAll()
                    .requestMatchers("/backup/**").denyAll()
                    .requestMatchers("/dev/**").denyAll()
                    .requestMatchers("/debug/**").denyAll()
                    // 데이터베이스 관련
                    .requestMatchers("/**/*.sql").denyAll()
                    .requestMatchers("/phpmyadmin/**").denyAll()
                    .requestMatchers("/mysql/**").denyAll()
                    // 압축 파일들
                    .requestMatchers("/**/*.zip").denyAll()
                    .requestMatchers("/**/*.tar.gz").denyAll()
                    .requestMatchers("/**/*.rar").denyAll()
                    .requestMatchers("/**/*.bak").denyAll()
                    // CMS 관련
                    .requestMatchers("/cms/**").denyAll()
                    .requestMatchers("/drupal/**").denyAll()
                    .requestMatchers("/joomla/**").denyAll()
                    // 서버 관련
                    .requestMatchers("/server-info").denyAll()
                    .requestMatchers("/server-status").denyAll()
                    .requestMatchers("/cgi-bin/**").denyAll()
                    // Docker 관련
                    .requestMatchers("/containers/json").denyAll()
                    // 기타 취약점 경로들
                    .requestMatchers("/developmentserver/**").denyAll()
                    .requestMatchers("/actuator/gateway/routes").denyAll()
                    .requestMatchers("/_layouts/**").denyAll()
                    .requestMatchers("/geoserver/**").denyAll()
                    .requestMatchers("/webui/**").denyAll()
                    // Spring Boot Actuator 보안 (필요한 것만 허용)
                    .requestMatchers("/actuator/**").denyAll()
                    // 모든 다른 요청은 인증 필요
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
            }
            .exceptionHandling { it.authenticationEntryPoint(jwtAuthenticationEntryPoint) }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf("http://localhost:3000")
        config.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        config.allowedHeaders = listOf("Authorization", "Content-Type", "Accept", "X-Requested-With")
        config.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }
}
