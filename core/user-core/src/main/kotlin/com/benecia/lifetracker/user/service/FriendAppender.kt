package com.benecia.lifetracker.user.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.user.exception.FriendErrorCode
import com.benecia.lifetracker.user.model.command.NewFriend
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FriendAppender(
    private val userReader: UserReader,
    private val friendRepository: FriendRepository,
) {
    fun add(userId: UUID, command: NewFriend): Long {
        userReader.findById(command.receiverId)
        if (friendRepository.exists(userId, command.receiverId)) {
            throw CoreException(FriendErrorCode.ALREADY_REQUESTED)
        }

        return friendRepository.add(userId, command.receiverId)
    }
}
