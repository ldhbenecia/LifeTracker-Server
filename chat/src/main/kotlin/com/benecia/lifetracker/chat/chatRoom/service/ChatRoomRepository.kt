package com.benecia.lifetracker.chat.chatRoom.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChatRoomRepository {
    fun findAllRoomsByUserId(userId: UUID): List<ChatRoom>
    fun findRoomsWithOpponents(userId: UUID): List<ChatRoomWithOpponent>
    fun createRoom(userId: UUID, opponentUserId: UUID): Long
    fun setNotification(userId: UUID, roomId: Long, enabled: Boolean): Long
    fun hideRoomForUser(userId: UUID, roomId: Long): Long
}
