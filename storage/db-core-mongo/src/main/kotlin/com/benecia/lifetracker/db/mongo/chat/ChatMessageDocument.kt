package com.benecia.lifetracker.db.mongo.chat

import com.benecia.lifetracker.chat.chatMessage.service.ChatMessage
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "chat_messages")
class ChatMessageDocument(
    @Id
    val id: String? = null,

    val roomId: String,

    val senderId: UUID,

    val senderName: String,

    val content: String,

    val timestamp: Long,
) {
    companion object {
        fun from(chatMessage: ChatMessage): ChatMessageDocument {
            return ChatMessageDocument(
                roomId = chatMessage.roomId,
                senderId = chatMessage.senderId,
                senderName = chatMessage.senderName,
                content = chatMessage.content,
                timestamp = chatMessage.timestamp,
            )
        }
    }

    fun toDomain(): ChatMessage = ChatMessage(
        id = this.id,
        roomId = this.roomId,
        senderId = this.senderId,
        senderName = this.senderName,
        content = this.content,
        timestamp = this.timestamp,
    )
}