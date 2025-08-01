package com.benecia.lifetracker.chat.chatRoom.model

import java.util.UUID

data class UserSummary(
    val userId: UUID,
    val userName: String,
    val userProfileImageUrl: String?,
)
