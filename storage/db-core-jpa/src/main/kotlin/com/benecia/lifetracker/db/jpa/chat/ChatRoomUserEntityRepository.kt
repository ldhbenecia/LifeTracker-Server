package com.benecia.lifetracker.db.jpa.chat

import com.benecia.lifetracker.chat.chatRoom.exception.ChatRoomErrorCode
import com.benecia.lifetracker.chat.chatRoom.service.ChatRoomUserRepository
import com.benecia.lifetracker.common.exception.CoreException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class ChatRoomUserEntityRepository(
    private val chatRoomUserJpaRepository: ChatRoomUserJpaRepository,
) : ChatRoomUserRepository {

    @Transactional
    override fun setNotification(userId: UUID, roomId: Long, enabled: Boolean): Long {
        val user = chatRoomUserJpaRepository.findById(ChatRoomUserId(roomId, userId))
            .orElseThrow { CoreException(ChatRoomErrorCode.CHAT_ROOM_USER_NOT_FOUND) }
        user.notificationEnabled = enabled
        chatRoomUserJpaRepository.save(user)
        return roomId
    }

    @Transactional
    override fun delete(userId: UUID, roomId: Long): Long {
        val user = chatRoomUserJpaRepository.findById(ChatRoomUserId(roomId, userId))
            .orElseThrow { CoreException(ChatRoomErrorCode.CHAT_ROOM_USER_NOT_FOUND) }
        user.visible = false
        chatRoomUserJpaRepository.save(user)
        return roomId
    }
}
