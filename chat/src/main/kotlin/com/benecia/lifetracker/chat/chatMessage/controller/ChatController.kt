package com.benecia.lifetracker.chat.chatMessage.controller

import com.benecia.lifetracker.chat.chatMessage.service.ChatMessage
import com.benecia.lifetracker.chat.chatMessage.service.ChatMessageProducer
import com.benecia.lifetracker.chat.chatMessage.service.ChatMessageRequest
import com.benecia.lifetracker.chat.chatMessage.service.ChatMessageService
import com.benecia.lifetracker.security.userdetails.LoginUser
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class ChatController(
    private val chatMessageProducer: ChatMessageProducer,
    private val chatMessageService: ChatMessageService,

) {
    @MessageMapping("chat.message.{roomId}")
    fun sendChatMessage(
        @DestinationVariable roomId: String,
        message: ChatMessageRequest,
        headerAccessor: SimpMessageHeaderAccessor,
    ) {
        val loginUser = headerAccessor.sessionAttributes?.get("loginUser") as? LoginUser
        val enriched = ChatMessage(
            roomId = roomId,
            senderId = loginUser?.id ?: UUID(0, 0),
            senderName = loginUser?.displayName ?: "unknown",
            content = message.content,
        )
        chatMessageProducer.sendChatMessage(enriched)
        chatMessageService.backupMessage(enriched)
    }
}
