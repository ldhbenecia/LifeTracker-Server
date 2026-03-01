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
                friendProvider = friendUser.provider,
                friendDisplayName = friendUser.displayName,
                friendProfileImageUrl = friendUser.profileImageUrl,
            )
        }
    }

    fun findPendingRequests(userId: UUID): List<FriendInfo> {
        val friends = friendRepository.findPendingRequestsByReceiverId(userId)

        return friends.map {
            val requester = userReader.findById(it.requesterId)
            FriendInfo(
                id = it.id!!,
                friendId = requester.id,
                friendProvider = requester.provider,
                friendDisplayName = requester.displayName,
                friendProfileImageUrl = requester.profileImageUrl,
            )
        }
    }

    fun findSentRequests(userId: UUID): List<FriendInfo> {
        val friends = friendRepository.findPendingRequestsByRequesterId(userId)

        return friends.map {
            val receiver = userReader.findById(it.receiverId)
            FriendInfo(
                id = it.id!!,
                friendId = receiver.id,
                friendProvider = receiver.provider,
                friendDisplayName = receiver.displayName,
                friendProfileImageUrl = receiver.profileImageUrl,
            )
        }
    }
}
