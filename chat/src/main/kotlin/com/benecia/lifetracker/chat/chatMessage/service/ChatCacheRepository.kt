package com.benecia.lifetracker.chat.chatMessage.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class ChatCacheRepository(
    private val redisTemplate: RedisTemplate<String, Any>
) {

    private val CHAT_CACHE_KEY_PREFIX = "chat:messages:"
    private val MAX_MESSAGES_IN_CACHE = 5000 // ZSET 최대 유지 개수

    fun saveToZSet(message: ChatMessage) {
        val key = CHAT_CACHE_KEY_PREFIX + message.roomId
        val score = message.timestamp.toDouble()
        val value = message

        redisTemplate.opsForZSet().add(key, value, score)

        // 캐시 크기 관리: 오래된 메시지 제거 (0부터 -5001까지 제거)
        // 가장 최근 5000개만 남김
        val start: Long = 0L
        val end: Long = -(MAX_MESSAGES_IN_CACHE + 1).toLong()
        redisTemplate.opsForZSet().removeRange(key, start, end)
    }
}