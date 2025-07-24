package com.benecia.lifetracker.chat.dto

import java.util.UUID

data class ChatMessage(
    val roomId: String,
    val senderId: UUID,
    val senderName: String,
    val content: String,
    val timestamp: Long
)
