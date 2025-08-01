package com.benecia.lifetracker.chat.chatRoom.service

import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomSummary
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChatRoomService(
    private val chatRoomReader: ChatRoomReader,
    private val chatRoomWriter: ChatRoomWriter
) {

    fun readRooms(userId: UUID): List<ChatRoomSummary> {
        return chatRoomReader.readRooms(userId)
    }

    fun createRoom(myUserId: UUID, opponentUserId: UUID): Long {
        return chatRoomWriter.createRoom(myUserId, opponentUserId)
    }

    fun hideRoom(userId: UUID, roomId: Long): Long {
        return chatRoomWriter.hideRoom(userId, roomId)
    }

    fun setNotification(userId: UUID, roomId: Long, enabled: Boolean): Long {
        return chatRoomWriter.setNotification(userId, roomId, enabled)
    }

}