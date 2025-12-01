package com.benecia.lifetracker.db.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = connectionFactory

        // ObjectMapper 설정: Kotlin과 LocalDateTime 등을 처리하기 위함
        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule())
            // ... 추가 설정 (예: DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES = false)
        }

        // Value 직렬화: 객체(ChatMessage 등)를 JSON으로 변환하여 저장
        val jsonSerializer = Jackson2JsonRedisSerializer<Any>(objectMapper, Any::class.java)

        // Key 직렬화: String을 사용하여 읽기 쉽게 설정
        template.keySerializer = StringRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()

        // Value 직렬화 적용
        template.valueSerializer = jsonSerializer
        template.hashValueSerializer = jsonSerializer

        template.afterPropertiesSet()
        return template
    }
}
