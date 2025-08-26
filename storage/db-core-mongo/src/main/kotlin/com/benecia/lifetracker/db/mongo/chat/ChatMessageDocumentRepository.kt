package com.benecia.lifetracker.db.mongo.chat

import com.benecia.lifetracker.chat.chatMessage.service.ChatMessage
import com.benecia.lifetracker.chat.chatMessage.service.ChatMessageRepository
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

    override fun findAllByRoomId(roomId: Long): List<ChatMessage> {
        return chatMessageJpaRepository.findAllByRoomIdOrderByTimestampAsc(roomId)
            .map { it.toDomain() }
    }
}
