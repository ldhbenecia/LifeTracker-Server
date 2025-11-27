package com.benecia.lifetracker.chat.chatMessage.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class ChatCacheRepository(
    private val redisTemplate: RedisTemplate<String, Any>,
) {

    private val chatCacheKeyPrefix = "chat:messages:"
    private val maxMessagesInCache = 5000

    fun saveToZSet(message: ChatMessage) {
        val key = chatCacheKeyPrefix + message.roomId
        val score = message.timestamp.toDouble()
        val value = message

        redisTemplate.opsForZSet().add(key, value, score)

        // 캐시 크기 관리: 오래된 메시지 제거 (0부터 -5001까지 제거)
        // 가장 최근 5000개만 남김
        val start: Long = 0L
        val end: Long = -(maxMessagesInCache + 1).toLong()
        redisTemplate.opsForZSet().removeRange(key, start, end)
    }

    fun findMessagesByTimestampBefore(
        roomId: String,
        cursorTimestamp: Double,
        limit: Long,
    ): List<ChatMessage> {
        val key = chatCacheKeyPrefix + roomId
        val messageSet = redisTemplate.opsForZSet().reverseRangeByScore(
            key,
            0.0, // Score 최소값 (가장 오래된 메시지)
            cursorTimestamp, // Score 최대값 (커서가 가리키는 시점)
            0, // offset (페이지 시작)
            limit,
        )

        return messageSet?.mapNotNull { it as? ChatMessage } ?: emptyList()
    }
}
