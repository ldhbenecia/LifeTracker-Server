package com.benecia.lifetracker.chat.chatRoom.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChatRoomUserRepository {
    fun setNotification(userId: UUID, roomId: Long, enabled: Boolean): Long
    fun delete(userId: UUID, roomId: Long): Long
    fun findNotificationTargets(roomId: Long, senderId: UUID): List<UUID>
}
