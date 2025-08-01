package com.benecia.lifetracker.chat.chatMessage.service

import com.benecia.lifetracker.common.config.QueueNames
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class ChatMessageProducer(
    private val rabbitTemplate: RabbitTemplate,
) {

    /**
     * 메세지를 RabbitMQ 큐에 발행
     */
    fun sendChatMessage(message: ChatMessage) {
        rabbitTemplate.convertAndSend(QueueNames.CHAT_QUEUE, message)
    }
}
