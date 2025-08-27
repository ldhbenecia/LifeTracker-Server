package com.benecia.lifetracker.chat.chatMessage.service

import org.springframework.stereotype.Service

@Service
class ChatMessageService(
    private val chatMessageReader: ChatMessageReader,
    private val chatMessageWriter: ChatMessageWriter,
) {

    fun getMessages(roomId: Long, lastMessageTimestamp: Long?, size: Int): List<ChatMessage> {
        return chatMessageReader.getMessages(roomId, lastMessageTimestamp, size)
    }

    fun backupMessage(message: ChatMessage): ChatMessage {
        return chatMessageWriter.backup(message)
    }
}
