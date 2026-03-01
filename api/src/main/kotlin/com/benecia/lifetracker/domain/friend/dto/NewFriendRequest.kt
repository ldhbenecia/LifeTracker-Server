package com.benecia.lifetracker.domain.friend.dto

import com.benecia.lifetracker.user.model.command.NewFriend

data class NewFriendRequest(
    val receiverCode: String,
) {
    fun toNewFriend(): NewFriend = NewFriend(receiverCode = receiverCode)
}
