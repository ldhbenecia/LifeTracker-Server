package com.benecia.lifetracker.notification.chat

import com.benecia.lifetracker.chat.chatMessage.service.ChatMessage
import com.benecia.lifetracker.chat.chatRoom.service.ChatRoomUserRepository
import com.benecia.lifetracker.notification.FcmNotificationService
import com.benecia.lifetracker.user.service.FcmTokenService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ChatFcmNotificationConsumer(
    private val chatRoomUserRepository: ChatRoomUserRepository,
    private val fcmTokenService: FcmTokenService,
    private val fcmNotificationService: FcmNotificationService,
) {
    @RabbitListener(queues = ["\${rabbitmq.fcm-queue.name}"])
    fun handleChatMessage(message: ChatMessage) {
        val receiverIds = chatRoomUserRepository.findNotificationTargets(message.roomId, message.senderId)
        receiverIds.forEach { receiverId ->
            fcmTokenService.findAllByUserId(receiverId).forEach { fcmToken ->
                fcmNotificationService.send(
                    token = fcmToken.token,
                    title = "새 메시지",
                    body = "${message.senderName}: ${message.content}",
                    data = mapOf("type" to "chat_message", "id" to message.roomId.toString()),
                )
            }
        }
    }
}
