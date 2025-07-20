package com.benecia.lifetracker.user.service

import java.util.UUID

data class Friend(
    val id: Long? = null,
    val requesterId: UUID,
    val receiverId: UUID,
    var status: FriendStatus,
)
