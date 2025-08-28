package com.benecia.lifetracker.chat.chatMessage.service

import com.benecia.lifetracker.chat.chatRoom.service.ChatRoomService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ChatMessageBackupConsumer(
    private val chatMessageService: ChatMessageService,
    private val chatRoomService: ChatRoomService,
) {

    private val logger = LoggerFactory.getLogger(ChatMessageBackupConsumer::class.java)

    @RabbitListener(queues = ["#{rabbitProperties.backupQueue.name}"])
    fun handleMessageBackup(message: ChatMessage) {
        try {
            chatMessageService.backupMessage(message)
            chatRoomService.updateLastMessage(message.roomId, message.content)
            logger.info("비동기 메시지 백업 및 업데이트 성공. Room ID: {}", message.roomId)
        } catch (e: Exception) {
            logger.error("비동기 메시지 백업 실패. Message: {}", message, e)
        }
    }
}
