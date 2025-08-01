package com.benecia.lifetracker.chat.chatRoom.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChatRoomRepository {
    fun findAllRoomsByUserId(userId: UUID): List<ChatRoom>
    fun findOpponentUserId(userId: UUID, roomId: Long): UUID
    fun createRoom(userId: UUID, opponentUserId: UUID): String
    fun leaveRoom(userId: UUID, roomId: Long)
    fun setNotification(userId: UUID, roomId: Long, enabled: Boolean)
}