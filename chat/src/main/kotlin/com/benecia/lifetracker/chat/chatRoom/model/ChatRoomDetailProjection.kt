package com.benecia.lifetracker.chat.chatRoom.model

import java.time.LocalDateTime
import java.util.UUID

data class ChatRoomDetailProjection(
    val roomId: Long,
    val lastMessage: String?,
    val lastMessageTime: LocalDateTime?,
    val opponentUserId: UUID,
    val opponentUserName: String,
    val opponentUserProfileImageUrl: String?,
    val myNotificationEnabled: Boolean,
    val myVisible: Boolean,
)
