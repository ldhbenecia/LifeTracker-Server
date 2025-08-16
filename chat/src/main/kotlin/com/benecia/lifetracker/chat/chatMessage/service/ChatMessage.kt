package com.benecia.lifetracker.chat.chatMessage.service

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val roomId: String,
    val senderId: UUID,
    val senderName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
)
