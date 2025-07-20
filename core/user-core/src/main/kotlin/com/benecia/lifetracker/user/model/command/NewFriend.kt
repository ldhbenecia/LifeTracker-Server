package com.benecia.lifetracker.user.model.command

import java.util.UUID

data class NewFriend(
    val receiverId: UUID,
)
