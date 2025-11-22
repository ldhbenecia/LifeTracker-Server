package com.benecia.lifetracker.chat.chatMessage.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ChatMessageWriter(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatCacheRepository: ChatCacheRepository,
) {

    private val logger = LoggerFactory.getLogger(ChatMessageWriter::class.java)

    fun backup(message: ChatMessage): ChatMessage {
        val savedMessage = chatMessageRepository.save(message)
        logger.info("MongoDB에 메시지 백업 완료. ID: {}", savedMessage.id)

        try {
            chatCacheRepository.saveToZSet(savedMessage)
            logger.info("Redis ZSET에 메시지 캐싱 완료. Room ID: {}", savedMessage.roomId)
        } catch (e: Exception) {
            logger.error("Redis 캐시 업데이트 실패. Message ID: {}", savedMessage.id, e)
        }

        return savedMessage
    }
}
