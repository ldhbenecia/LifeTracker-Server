package com.benecia.lifetracker.chat.controller

import com.benecia.lifetracker.chat.dto.ChatMessage
import com.benecia.lifetracker.chat.service.ChatMessageProducer
import com.benecia.lifetracker.security.userdetails.LoginUser
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class ChatController(
    private val chatMessageProducer: ChatMessageProducer,
) {
    @MessageMapping("/chat.send")
    fun sendChatMessage(
        message: ChatMessage,
        headerAccessor: SimpMessageHeaderAccessor,
    ) {
        val loginUser = headerAccessor.sessionAttributes?.get("loginUser") as? LoginUser
        chatMessageProducer.sendChatMessage(
            message.copy(
                senderId = loginUser?.id ?: UUID(0, 0),
                senderName = loginUser?.email ?: "unknown",
            ),
        )
    }
}
