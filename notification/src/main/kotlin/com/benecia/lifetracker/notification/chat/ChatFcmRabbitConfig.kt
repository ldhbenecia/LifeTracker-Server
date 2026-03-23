package com.benecia.lifetracker.notification.chat

import com.benecia.lifetracker.chat.config.RabbitProperties
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatFcmRabbitConfig(
    @Value("\${rabbitmq.fcm-queue.name}") private val fcmQueueName: String,
    private val chatExchange: TopicExchange,
    private val rabbitProperties: RabbitProperties,
) {
    @Bean
    fun fcmQueue(): Queue = Queue(fcmQueueName, true)

    @Bean
    fun fcmBinding(): Binding = BindingBuilder
        .bind(fcmQueue())
        .to(chatExchange)
        .with(rabbitProperties.chatRouting.key + ".#")
}
