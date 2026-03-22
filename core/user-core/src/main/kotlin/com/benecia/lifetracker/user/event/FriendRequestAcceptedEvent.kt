package com.benecia.lifetracker.user.event

import java.util.UUID

data class FriendRequestAcceptedEvent(
    val friendRequestId: Long,
    val requesterId: UUID,
    val receiverId: UUID,
)
