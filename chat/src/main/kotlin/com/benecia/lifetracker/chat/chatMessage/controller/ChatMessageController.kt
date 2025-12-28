package com.benecia.lifetracker.chat.chatMessage.controller

import com.benecia.lifetracker.chat.chatMessage.service.ChatMessage
import com.benecia.lifetracker.chat.chatMessage.service.ChatMessageProducer
import com.benecia.lifetracker.chat.chatMessage.service.ChatMessageRequest
import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.user.exception.UserErrorCode
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller

@Controller
class ChatMessageController(
    private val chatMessageProducer: ChatMessageProducer,
) {

    private val logger = LoggerFactory.getLogger(ChatMessageController::class.java)

    @MessageMapping("chat.message.{roomId}")
    fun sendChatMessage(
        @DestinationVariable roomId: Long,
        message: ChatMessageRequest,
        headerAccessor: SimpMessageHeaderAccessor,
    ) {
        val loginUser = headerAccessor.sessionAttributes?.get("loginUser") as? LoginUser
            ?: throw CoreException(UserErrorCode.INVALID_ACCESS_TOKEN)
        logger.debug("Sending message to Room ID: {}", roomId)

        val enriched = ChatMessage(
            roomId = roomId,
            senderId = loginUser.id,
            senderName = loginUser.displayName,
            content = message.content,
        )

        chatMessageProducer.sendChatMessage(enriched)
        logger.info("Message sent and backed up for room ID: {}", roomId)
    }
}
