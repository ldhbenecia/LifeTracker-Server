package com.benecia.lifetracker.chat.chatMessage.service

import org.springframework.stereotype.Repository

@Repository
interface ChatCacheRepository {
    fun saveToZSet(message: ChatMessage)
    fun findMessagesByTimestampBefore(roomId: String, cursorTimestamp: Double, limit: Long): List<ChatMessage>
}
