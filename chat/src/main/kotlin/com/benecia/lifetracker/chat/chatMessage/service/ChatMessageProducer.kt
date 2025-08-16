package com.benecia.lifetracker.chat.chatMessage.service

import com.benecia.lifetracker.chat.config.RabbitProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class ChatMessageProducer(
    private val rabbitTemplate: RabbitTemplate,
    private val rabbitProperties: RabbitProperties,
) {

    fun sendChatMessage(message: ChatMessage) {
        val routingKey = rabbitProperties.chatRouting.key + "." + message.roomId
        rabbitTemplate.convertAndSend(rabbitProperties.chatExchange.name, routingKey, message)
    }
}
