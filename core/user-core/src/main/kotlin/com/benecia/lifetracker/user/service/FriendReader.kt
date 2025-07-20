package com.benecia.lifetracker.user.service

import com.benecia.lifetracker.user.model.info.FriendInfo
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FriendReader(
    private val userReader: UserReader,
    private val friendRepository: FriendRepository,
) {
    fun findAllByUserId(userId: UUID): List<FriendInfo> {
        val friends = friendRepository.findAllByUserId(userId)

        return friends.map {
            val friendId = if (it.requesterId == userId) it.receiverId else it.requesterId
            val friendUser = userReader.findById(friendId)
            FriendInfo(
                id = it.id!!,
                friendId = friendId,
                friendDisplayName = friendUser.displayName,
                friendProfileImageUrl = friendUser.profileImageUrl,
            )
        }
    }
}
