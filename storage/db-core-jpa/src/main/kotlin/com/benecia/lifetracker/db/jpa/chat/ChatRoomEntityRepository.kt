package com.benecia.lifetracker.db.jpa.chat

import com.benecia.lifetracker.chat.chatRoom.exception.ChatRoomErrorCode
import com.benecia.lifetracker.chat.chatRoom.service.ChatRoom
import com.benecia.lifetracker.chat.chatRoom.service.ChatRoomRepository
import com.benecia.lifetracker.chat.chatRoom.service.ChatRoomWithOpponent
import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.common.exception.ErrorCode
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class ChatRoomEntityRepository(
    private val chatRoomJpaRepository: ChatRoomJpaRepository,
    private val chatRoomUserJpaRepository: ChatRoomUserJpaRepository
) : ChatRoomRepository {

    override fun findAllRoomsByUserId(userId: UUID): List<ChatRoom> {
        return chatRoomJpaRepository.findAllRoomsByUserId(userId)
            .map { entity ->
                ChatRoom(
                    id = entity.id,
                    lastMessage = entity.lastMessage,
                    lastMessageTime = entity.lastMessageTime
                )
            }
    }

    override fun findRoomsWithOpponents(userId: UUID): List<ChatRoomWithOpponent> {
        return chatRoomJpaRepository.findRoomsWithOpponents(userId)
    }

    @Transactional
    override fun createRoom(userId: UUID, opponentUserId: UUID): Long {
        val existingRoomId = chatRoomUserJpaRepository.findRoomIdByUserIds(userId, opponentUserId)
        if (existingRoomId != null) {
            return existingRoomId
        }

        val chatRoom = ChatRoomEntity()
        chatRoomJpaRepository.save(chatRoom)

        val user1 = ChatRoomUserEntity(
            id = ChatRoomUserId(roomId = chatRoom.id!!, userId = userId)
        )
        val user2 = ChatRoomUserEntity(
            id = ChatRoomUserId(roomId = chatRoom.id!!, userId = opponentUserId)
        )
        chatRoomUserJpaRepository.saveAll(listOf(user1, user2))

        return chatRoom.id!!
    }

    override fun setNotification(userId: UUID, roomId: Long, enabled: Boolean): Long {
        val user = chatRoomUserJpaRepository.findById(ChatRoomUserId(roomId, userId))
            .orElseThrow { CoreException(ChatRoomErrorCode.CHAT_ROOM_USER_NOT_FOUND) }
        user.notificationEnabled = enabled
        chatRoomUserJpaRepository.save(user)
        return roomId
    }

    override fun hideRoomForUser(userId: UUID, roomId: Long): Long {
        val user = chatRoomUserJpaRepository.findById(ChatRoomUserId(roomId, userId))
            .orElseThrow { CoreException(ChatRoomErrorCode.CHAT_ROOM_USER_NOT_FOUND) }
        user.visible = false
        chatRoomUserJpaRepository.save(user)
        return roomId
    }
}