package com.benecia.lifetracker.chat.chatMessage.service

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class ChatMessageConsumer(
    private val messagingTemplate: SimpMessagingTemplate,
) {

    @RabbitListener(queues = ["#{rabbitProperties.chatQueue.name}"])
    fun receiveChatMessage(message: ChatMessage) {
        messagingTemplate.convertAndSend("/topic/chat.${message.roomId}", message)
    }
}
