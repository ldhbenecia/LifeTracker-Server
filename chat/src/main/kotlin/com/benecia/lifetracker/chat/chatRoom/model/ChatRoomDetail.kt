package com.benecia.lifetracker.chat.chatRoom.model

import java.time.LocalDateTime

data class ChatRoomDetail(
    val roomId: Long,
    val lastMessage: String?,
    val lastMessageTime: LocalDateTime?,
    val opponent: UserSummary,
    val myNotificationEnabled: Boolean,
    val myVisible: Boolean,
)
