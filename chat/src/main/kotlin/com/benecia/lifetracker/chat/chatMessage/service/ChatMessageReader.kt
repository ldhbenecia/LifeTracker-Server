package com.benecia.lifetracker.chat.chatMessage.service

import org.springframework.stereotype.Component

@Component
class ChatMessageReader(
    private val chatMessageRepository: ChatMessageRepository,
) {
    fun getMessages(roomId: String): List<ChatMessage> {
        return chatMessageRepository.findAllByRoomId(roomId)
    }
}
