package com.benecia.lifetracker.domain.friend.dto

import com.benecia.lifetracker.user.model.command.NewFriend

data class NewFriendRequest(
    val receiverEmail: String,
) {
    fun toNewFriend(): NewFriend {
        return NewFriend(
            receiverEmail = this.receiverEmail,
        )
    }
}
