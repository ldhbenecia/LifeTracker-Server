package com.benecia.lifetracker.chat.chatRoom.service

import java.time.LocalDateTime

data class ChatRoom(
    val id: Long? = null,
    val lastMessage: String?,
    val lastMessageTime: LocalDateTime?
)
