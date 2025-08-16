package com.benecia.lifetracker.chat.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
class RabbitProperties {
    lateinit var chatQueue: ChatQueue
    lateinit var chatExchange: ChatExchange
    lateinit var chatRouting: ChatRouting

    class ChatQueue {
        lateinit var name: String
    }
    class ChatExchange {
        lateinit var name: String
    }
    class ChatRouting {
        lateinit var key: String
    }
}
