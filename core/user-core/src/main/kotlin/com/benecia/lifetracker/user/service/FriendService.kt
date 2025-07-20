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
}
