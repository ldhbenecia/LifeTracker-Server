package com.benecia.lifetracker.user.model.info

import java.util.UUID

data class FriendInfo(
    val id: Long,
    val friendId: UUID,
    val friendDisplayName: String,
    val friendProfileImageUrl: String?,
)
