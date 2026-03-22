package com.benecia.lifetracker.user.event

import java.util.UUID

data class FriendRequestSentEvent(
    val friendRequestId: Long,
    val requesterId: UUID,
    val receiverId: UUID,
)
