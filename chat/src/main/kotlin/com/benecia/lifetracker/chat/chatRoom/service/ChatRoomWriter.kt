package com.benecia.lifetracker.chat.chatRoom.service

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ChatRoomWriter(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomUserRepository: ChatRoomUserRepository,
) {

    fun createRoom(userId: UUID, opponentUserId: UUID): Long {
        return chatRoomRepository.createRoom(userId, opponentUserId)
    }

    fun removeRoom(userId: UUID, roomId: Long): Long {
        return chatRoomUserRepository.delete(userId, roomId)
    }

    fun setNotification(userId: UUID, roomId: Long, enabled: Boolean): Long {
        return chatRoomUserRepository.setNotification(userId, roomId, enabled)
    }
}
