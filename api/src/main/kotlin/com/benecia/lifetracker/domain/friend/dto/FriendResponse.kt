package com.benecia.lifetracker.domain.friend.dto

import com.benecia.lifetracker.user.model.info.FriendInfo
import java.util.UUID

data class FriendResponse(
    val id: Long,
    val friendId: UUID,
    val friendProvider: String,
    val friendDisplayName: String,
    val friendProfileImageUrl: String?,
) {
    companion object {
        fun of(info: FriendInfo): FriendResponse {
            return FriendResponse(
                id = info.id,
                friendId = info.friendId,
                friendProvider = info.friendProvider,
                friendDisplayName = info.friendDisplayName,
                friendProfileImageUrl = info.friendProfileImageUrl,
            )
        }
    }
}
