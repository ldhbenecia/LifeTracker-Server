package com.benecia.lifetracker.chat.chatRoom.service

import java.time.LocalDateTime
import java.util.UUID

data class ChatRoomWithOpponent(
    val roomId: Long,
    val lastMessage: String?,
    val lastMessageTime: LocalDateTime?,
    val opponentUserId: UUID,
    val opponentUserName: String,
    val opponentProfileImageUrl: String?,
)
