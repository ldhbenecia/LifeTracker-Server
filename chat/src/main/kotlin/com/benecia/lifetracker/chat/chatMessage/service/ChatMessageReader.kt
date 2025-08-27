package com.benecia.lifetracker.chat.chatMessage.service

import org.springframework.stereotype.Component

@Component
class ChatMessageReader(
    private val chatMessageRepository: ChatMessageRepository,
) {
    fun getMessages(roomId: Long, lastMessageTimestamp: Long?, size: Int): List<ChatMessage> {
        return chatMessageRepository.findAllByRoomId(roomId, lastMessageTimestamp, size)
    }
}
