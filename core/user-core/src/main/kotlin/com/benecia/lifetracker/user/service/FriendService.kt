package com.benecia.lifetracker.user.service

import com.benecia.lifetracker.user.model.command.NewFriend
import com.benecia.lifetracker.user.model.info.FriendInfo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FriendService(
    private val friendReader: FriendReader,
    private val friendAppender: FriendAppender,
) {
    fun add(userId: UUID, command: NewFriend): Long {
        return friendAppender.add(userId, command)
    }

    fun findAllByUserId(userId: UUID): List<FriendInfo> {
        return friendReader.findAllByUserId(userId)
    }

    fun findPendingRequests(userId: UUID): List<FriendInfo> {
        return friendReader.findPendingRequests(userId)
    }

    fun acceptRequest(userId: UUID, friendRequestId: Long): Long {
        return friendAppender.acceptRequest(userId, friendRequestId)
    }

    fun rejectRequest(userId: UUID, friendRequestId: Long): Long {
        return friendAppender.rejectRequest(userId, friendRequestId)
    }
}
