package com.benecia.lifetracker.chat.chatMessage.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ChatMessageReader(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatCacheRepository: ChatCacheRepository
) {
    private val logger = LoggerFactory.getLogger(ChatMessageReader::class.java)

    fun getMessages(roomId: Long, lastMessageTimestamp: Long?, size: Int): List<ChatMessage> {
        val limit = size.toLong()
        val cursorTimestamp = lastMessageTimestamp?.toDouble() ?: Long.MAX_VALUE.toDouble()

        val cachedMessages = chatCacheRepository.findMessagesByTimestampBefore(
            roomId.toString(),
            cursorTimestamp,
            limit
        )

        if (cachedMessages.size.toLong() == limit) {
            logger.debug("Cache Hit: Retrieved {} messages from Redis for room {}", limit, roomId)
            return cachedMessages
        }

        // --- Cache Miss 또는 부족한 경우: DB Fallback
        // DB에서 조회할 커서: 캐시에서 찾은 가장 오래된 메시지의 시간
        val dbCursor = cachedMessages.lastOrNull()?.timestamp ?: lastMessageTimestamp
        val neededCount = limit - cachedMessages.size.toLong()
        if (neededCount <= 0) {
            return cachedMessages
        }

        logger.debug("Cache Miss: Retrieved {} messages from MongoDB (Needed {})", neededCount, neededCount)

        // MongoDB 조회 (Fallback)
        val dbMessages = chatMessageRepository.findAllByRoomIdAndTimestampBefore(
            roomId,
            dbCursor,
            neededCount
        )

        val combinedMessages = (cachedMessages + dbMessages).distinct()
        return combinedMessages.sortedByDescending { it.timestamp }.take(size)
    }
}
