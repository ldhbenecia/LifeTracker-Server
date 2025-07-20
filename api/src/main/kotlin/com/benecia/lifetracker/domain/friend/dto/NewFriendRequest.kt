package com.benecia.lifetracker.domain.friend.dto

import com.benecia.lifetracker.user.model.command.NewFriend
import java.util.UUID

data class NewFriendRequest(
    val receiverId: UUID,
) {
    fun toNewFriend(): NewFriend {
        return NewFriend(
            receiverId = this.receiverId,
        )
    }
}
