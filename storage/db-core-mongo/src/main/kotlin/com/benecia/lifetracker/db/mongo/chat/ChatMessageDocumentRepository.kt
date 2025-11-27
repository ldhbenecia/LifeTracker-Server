package com.benecia.lifetracker.db.mongo.chat

import com.benecia.lifetracker.chat.chatMessage.service.ChatMessage
import com.benecia.lifetracker.chat.chatMessage.service.ChatMessageRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class ChatMessageDocumentRepository(
    private val chatMessageJpaRepository: ChatMessageJpaRepository,
) : ChatMessageRepository {
    override fun save(message: ChatMessage): ChatMessage {
        val document = ChatMessageDocument.from(message)
        val saved = chatMessageJpaRepository.save(document)
        return saved.toDomain()
    }

    override fun findAllByRoomId(roomId: Long, lastMessageTimestamp: Long?, size: Int): List<ChatMessage> {
        val sort = Sort.by(Sort.Direction.DESC, "timestamp")
        val pageable = PageRequest.of(0, size, sort)

        val documents = if (lastMessageTimestamp == null) {
            chatMessageJpaRepository.findByRoomId(roomId, pageable)
        } else {
            chatMessageJpaRepository.findByRoomIdAndTimestampLessThan(roomId, lastMessageTimestamp, pageable)
        }

        return documents.map { it.toDomain() }.reversed()
    }

    override fun findAllByRoomIdAndTimestampBefore(roomId: Long, timestamp: Long?, limit: Long): List<ChatMessage> {
        val sort = Sort.by(Sort.Direction.DESC, "timestamp")
        val pageable = PageRequest.of(0, limit.toInt(), sort)

        val documents = chatMessageJpaRepository.findByRoomIdAndTimestampLessThanEqual(
            roomId,
            timestamp,
            pageable
        )

        return documents.map { it.toDomain() }
    }
}
