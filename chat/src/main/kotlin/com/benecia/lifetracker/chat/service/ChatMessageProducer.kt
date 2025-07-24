package com.benecia.lifetracker.chat.service

import com.benecia.lifetracker.common.config.QueueNames
import com.benecia.lifetracker.chat.dto.ChatMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class ChatMessageProducer(
    private val rabbitTemplate: RabbitTemplate
) {
    fun sendChatMessage(message: ChatMessage) {
        rabbitTemplate.convertAndSend(QueueNames.CHAT_QUEUE, message)
    }
}