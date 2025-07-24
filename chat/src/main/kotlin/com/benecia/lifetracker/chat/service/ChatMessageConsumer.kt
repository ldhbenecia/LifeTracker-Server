package com.benecia.lifetracker.chat.service

import com.benecia.lifetracker.common.config.QueueNames
import com.benecia.lifetracker.chat.dto.ChatMessage
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.simp.SimpMessagingTemplate

class ChatMessageConsumer(
    private val messagingTemplate: SimpMessagingTemplate
) {
    @RabbitListener(queues = [QueueNames.CHAT_QUEUE])
    fun receiveChatMessage(message: ChatMessage) {
        messagingTemplate.convertAndSend("/topic/chat/${message.roomId}", message)
    }
}