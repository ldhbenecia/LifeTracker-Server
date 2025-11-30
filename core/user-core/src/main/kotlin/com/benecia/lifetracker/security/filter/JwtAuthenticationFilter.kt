package com.benecia.lifetracker.security.filter

import com.benecia.lifetracker.util.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)

        try {
            if (jwtUtil.validateToken(token)) {
                val authentication = jwtUtil.getAuthentication(token)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (exception: Exception) {
            log.debug(
                "Unauthorized request due to JWT error: {} | Request: {} {} | IP: {} | User-Agent: {} | Auth: {}",
                exception.message,
                request.method,
                request.requestURI,
                request.remoteAddr,
                request.getHeader("User-Agent"),
                authHeader,
            )
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }
}
