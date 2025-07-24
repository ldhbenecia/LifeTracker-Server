package com.benecia.lifetracker.chat.config

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.security.userdetails.LoginUserDetailsService
import com.benecia.lifetracker.user.exception.UserErrorCode
import com.benecia.lifetracker.util.JwtUtil
import org.springframework.http.HttpHeaders
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class AuthHandshakeInterceptor(
    private val loginUserDetailsService: LoginUserDetailsService,
    private val jwtUtil: JwtUtil,
) : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>,
    ): Boolean {
        val token = request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.removePrefix("Bearer ")
            ?: throw CoreException(UserErrorCode.INVALID_ACCESS_TOKEN)
        val userId = jwtUtil.extractUserId(token)
        val loginUser = loginUserDetailsService.loadUserByUsername(userId) as LoginUser
        attributes["loginUser"] = loginUser
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?,
    ) {}
}
