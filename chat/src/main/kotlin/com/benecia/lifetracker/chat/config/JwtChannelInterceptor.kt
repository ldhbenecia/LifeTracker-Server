package com.benecia.lifetracker.chat.config

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.security.userdetails.LoginUser
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
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)

        if (StompCommand.CONNECT == accessor.command) {
            val token = accessor.getFirstNativeHeader("Authorization")
                ?.removePrefix("Bearer ")
                ?: throw CoreException(UserErrorCode.INVALID_ACCESS_TOKEN)

            if (jwtUtil.validateToken(token)) {
                val authentication = jwtUtil.getAuthentication(token)
                accessor.user = authentication

                if (authentication.principal is LoginUser) {
                    accessor.sessionAttributes?.put("loginUser", authentication.principal)
                }
            } else {
                throw CoreException(UserErrorCode.INVALID_ACCESS_TOKEN)
            }
        }

        return message
    }
}
