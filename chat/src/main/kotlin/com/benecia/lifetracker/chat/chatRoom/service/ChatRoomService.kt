package com.benecia.lifetracker.chat.chatRoom.service

import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomSummary
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChatRoomService(
    private val chatRoomReader: ChatRoomReader,
    private val chatRoomWriter: ChatRoomWriter,
) {

    fun findOrCreateRoom(senderId: UUID, opponentId: UUID): Long {
        val existingRoomId = chatRoomReader.findRoomByUsers(senderId, opponentId)
        return existingRoomId ?: chatRoomWriter.createRoom(senderId, opponentId)
    }

    fun readRooms(userId: UUID): List<ChatRoomSummary> {
        return chatRoomReader.readRooms(userId)
    }

    fun createRoom(userId: UUID, opponentUserId: UUID): Long {
        return chatRoomWriter.createRoom(userId, opponentUserId)
    }

    fun removeRoom(userId: UUID, roomId: Long): Long {
        return chatRoomWriter.removeRoom(userId, roomId)
    }

    fun setNotification(userId: UUID, roomId: Long, enabled: Boolean): Long {
        return chatRoomWriter.setNotification(userId, roomId, enabled)
    }
}
