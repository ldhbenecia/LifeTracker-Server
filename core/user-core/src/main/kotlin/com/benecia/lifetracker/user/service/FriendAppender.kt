package com.benecia.lifetracker.user.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.user.exception.FriendErrorCode
import com.benecia.lifetracker.user.exception.UserErrorCode
import com.benecia.lifetracker.user.model.command.NewFriend
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FriendAppender(
    private val userReader: UserReader,
    private val friendRepository: FriendRepository,
) {
    fun add(userId: UUID, command: NewFriend): Long {
        val receiver = userReader.findByUserCode(command.receiverCode)
            ?: throw CoreException(UserErrorCode.USER_NOT_FOUND)
        val receiverId = receiver.id!!

        if (friendRepository.exists(userId, receiverId)) {
            throw CoreException(FriendErrorCode.ALREADY_REQUESTED)
        }

        val friend = Friend(
            requesterId = userId,
            receiverId = receiverId,
            status = FriendStatus.PENDING,
        )

        return friendRepository.add(friend)
    }

    fun acceptRequest(userId: UUID, friendRequestId: Long): Long {
        val request = friendRepository.findFriendRequestById(friendRequestId)
        if (request.receiverId != userId) {
            throw CoreException(UserErrorCode.USER_NOT_FOUND)
        }

        return friendRepository.changeFriendRequestStatus(friendRequestId, FriendStatus.ACCEPTED)
    }

    fun rejectRequest(userId: UUID, friendRequestId: Long): Long {
        val request = friendRepository.findFriendRequestById(friendRequestId)
        if (request.receiverId != userId) {
            throw CoreException(UserErrorCode.USER_NOT_FOUND)
        }

        return friendRepository.changeFriendRequestStatus(friendRequestId, FriendStatus.REJECTED)
    }

    fun cancelRequest(userId: UUID, friendRequestId: Long): Long {
        val request = friendRepository.findFriendRequestById(friendRequestId)
        if (request.requesterId != userId) {
            throw CoreException(UserErrorCode.FORBIDDEN_USER_ACCESS)
        }
        if (request.status != FriendStatus.PENDING) {
            throw CoreException(FriendErrorCode.CANNOT_CANCEL_REQUEST)
        }
        friendRepository.delete(userId, friendRequestId)
        return friendRequestId
    }

    fun delete(userId: UUID, friendId: Long): Long {
        friendRepository.delete(userId, friendId)
        return friendId
    }
}
