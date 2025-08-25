package com.benecia.lifetracker.chat.chatMessage.service

import org.springframework.stereotype.Component

@Component
class ChatMessageWriter(
    private val chatMessageRepository: ChatMessageRepository,
) {
    fun backup(message: ChatMessage) {
        return chatMessageRepository.save(message)
    }
}
