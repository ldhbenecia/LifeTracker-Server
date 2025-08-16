package com.benecia.lifetracker.chat.config

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.security.userdetails.LoginUserDetailsService
import com.benecia.lifetracker.security.userdetails.LoginUserPrincipal
import com.benecia.lifetracker.user.exception.UserErrorCode
import com.benecia.lifetracker.util.JwtUtil
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class JwtChannelInterceptor(
    private val jwtUtil: JwtUtil,
    private val loginUserDetailsService: LoginUserDetailsService,
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)

        if (StompCommand.CONNECT == accessor.command) {
            val token = accessor.getFirstNativeHeader("Authorization")
                ?.removePrefix("Bearer ")
                ?: throw CoreException(UserErrorCode.INVALID_ACCESS_TOKEN)

            val userId = jwtUtil.extractUserId(token)
            val loginUser = loginUserDetailsService.loadUserByUsername(userId) as LoginUser

            accessor.user = LoginUserPrincipal(loginUser)
            accessor.sessionAttributes?.put("loginUser", loginUser)
        }

        return message
    }
}
