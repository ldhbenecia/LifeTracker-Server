package com.benecia.lifetracker.chat.chatRoom.service

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ChatRoomWriter(
    private val chatRoomRepository: ChatRoomRepository,
) {

    fun createRoom(myUserId: UUID, opponentUserId: UUID): Long {
        return chatRoomRepository.createRoom(myUserId, opponentUserId)
    }

    fun hideRoom(userId: UUID, roomId: Long): Long {
        return chatRoomRepository.hideRoomForUser(userId, roomId)
    }

    fun setNotification(userId: UUID, roomId: Long, enabled: Boolean): Long {
        return chatRoomRepository.setNotification(userId, roomId, enabled)
    }
}