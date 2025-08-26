package com.benecia.lifetracker.chat.chatMessage.service

import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository {
    fun save(message: ChatMessage): ChatMessage
    fun findAllByRoomId(roomId: Long): List<ChatMessage>
}
