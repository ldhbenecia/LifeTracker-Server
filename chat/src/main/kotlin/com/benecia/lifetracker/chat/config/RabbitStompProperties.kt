package com.benecia.lifetracker.chat.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "rabbitmq.stomp")
class RabbitStompProperties {
    lateinit var host: String
    var port: Int = 61613
    lateinit var username: String
    lateinit var password: String
}
