package com.benecia.lifetracker.chat.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig(
    private val rabbitProperties: RabbitProperties,
    private val stompProperties: RabbitStompProperties,
) {

    @Bean
    fun chatQueue(): Queue {
        return Queue(rabbitProperties.chatQueue.name, true)
    }

    @Bean
    fun chatExchange(): TopicExchange {
        return TopicExchange(rabbitProperties.chatExchange.name, true, false)
    }

    @Bean
    fun chatBinding(): Binding {
        return BindingBuilder
            .bind(chatQueue())
            .to(chatExchange())
            .with(rabbitProperties.chatRouting.key + ".*")
    }

    @Bean
    fun backupQueue(): Queue {
        return Queue(rabbitProperties.backupQueue.name, true)
    }

    @Bean
    fun backupBinding(chatExchange: TopicExchange, backupQueue: Queue): Binding {
        return BindingBuilder
            .bind(backupQueue)
            .to(chatExchange)
            .with(rabbitProperties.chatRouting.key + ".#")
    }

    @Bean
    fun rabbitTemplate(): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory())
        template.messageConverter = messageConverter()
        return template
    }

    @Bean
    fun rabbitMessagingTemplate(rabbitTemplate: RabbitTemplate): RabbitMessagingTemplate {
        return RabbitMessagingTemplate((rabbitTemplate))
    }

    @Bean
    fun connectionFactory(): CachingConnectionFactory {
        return CachingConnectionFactory(stompProperties.host).apply {
            username = stompProperties.username
            setPassword(stompProperties.password)
        }
    }

    @Bean
    fun messageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }
}
