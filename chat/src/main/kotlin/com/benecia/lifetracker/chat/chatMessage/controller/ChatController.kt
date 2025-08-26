package com.benecia.lifetracker.chat.chatMessage.controller

import com.benecia.lifetracker.chat.chatMessage.service.ChatMessage
import com.benecia.lifetracker.chat.chatMessage.service.ChatMessageProducer
import com.benecia.lifetracker.chat.chatMessage.service.ChatMessageRequest
import com.benecia.lifetracker.chat.chatMessage.service.ChatMessageService
import com.benecia.lifetracker.chat.chatRoom.service.ChatRoomService
import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.user.exception.UserErrorCode
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class ChatController(
    private val chatMessageProducer: ChatMessageProducer,
    private val chatMessageService: ChatMessageService,
    private val chatRoomService: ChatRoomService,
) {

    private val logger = LoggerFactory.getLogger(ChatController::class.java)

    @MessageMapping("chat.message.{opponentId}")
    fun sendChatMessage(
        @DestinationVariable opponentId: UUID,
        message: ChatMessageRequest,
        headerAccessor: SimpMessageHeaderAccessor,
    ) {
        val loginUser = headerAccessor.sessionAttributes?.get("loginUser") as? LoginUser
            ?: throw CoreException(UserErrorCode.INVALID_ACCESS_TOKEN)
        logger.info("Sending message from user: {} to opponent: {}", loginUser.id, opponentId)

        val roomId = chatRoomService.findOrCreateRoom(loginUser.id, opponentId)
        logger.info("Found or created chat room. Room ID: {}", roomId)

        val enriched = ChatMessage(
            roomId = roomId,
            senderId = loginUser.id,
            senderName = loginUser.displayName,
            content = message.content,
        )
        chatMessageProducer.sendChatMessage(enriched)
        chatMessageService.backupMessage(enriched)

        logger.info("Message sent and backed up for room ID: {}", roomId)
    }
}
