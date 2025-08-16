package com.benecia.lifetracker.chat.chatMessage.service

data class ChatMessageRequest(
    val roomId: String,
    val content: String,
)
