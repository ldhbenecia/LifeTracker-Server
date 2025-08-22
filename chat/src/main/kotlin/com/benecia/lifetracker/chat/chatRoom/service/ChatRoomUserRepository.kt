package com.benecia.lifetracker.chat.chatRoom.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChatRoomUserRepository {
    fun setNotification(userId: UUID, roomId: Long, enabled: Boolean): Long
    fun hideRoomForUser(userId: UUID, roomId: Long): Long
}
