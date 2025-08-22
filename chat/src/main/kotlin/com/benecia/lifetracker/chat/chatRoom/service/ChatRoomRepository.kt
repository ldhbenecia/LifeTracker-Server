package com.benecia.lifetracker.chat.chatRoom.service

import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomDetail
import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomWithOpponent
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChatRoomRepository {
    fun findAllRoomsByUserId(userId: UUID): List<ChatRoom>
    fun findRoomsWithOpponents(userId: UUID): List<ChatRoomWithOpponent>
    fun createRoom(userId: UUID, opponentUserId: UUID): Long
    fun findRoomDetail(userId: UUID, roomId: Long): ChatRoomDetail?
}
