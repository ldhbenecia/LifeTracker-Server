package com.benecia.lifetracker.db.jpa.chat

import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomDetail
import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomWithOpponent
import com.benecia.lifetracker.chat.chatRoom.model.UserSummary
import com.benecia.lifetracker.chat.chatRoom.service.ChatRoom
import com.benecia.lifetracker.chat.chatRoom.service.ChatRoomRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class ChatRoomEntityRepository(
    private val chatRoomJpaRepository: ChatRoomJpaRepository,
    private val chatRoomUserJpaRepository: ChatRoomUserJpaRepository,
) : ChatRoomRepository {

    override fun findAllRoomsByUserId(userId: UUID): List<ChatRoom> {
        return chatRoomJpaRepository.findAllRoomsByUserId(userId)
            .map { entity ->
                ChatRoom(
                    id = entity.id,
                    lastMessage = entity.lastMessage,
                    lastMessageTime = entity.lastMessageTime,
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
            id = ChatRoomUserId(roomId = chatRoom.id!!, userId = userId),
        )
        val user2 = ChatRoomUserEntity(
            id = ChatRoomUserId(roomId = chatRoom.id!!, userId = opponentUserId),
        )
        chatRoomUserJpaRepository.saveAll(listOf(user1, user2))

        return chatRoom.id!!
    }

    override fun findRoomDetail(userId: UUID, roomId: Long): ChatRoomDetail? {
        val projection = chatRoomJpaRepository.findRoomDetail(userId, roomId)
            ?: return null

        return ChatRoomDetail(
            roomId = projection.roomId,
            lastMessage = projection.lastMessage,
            lastMessageTime = projection.lastMessageTime,
            opponent = UserSummary(
                userId = projection.opponentUserId,
                userName = projection.opponentUserName,
                userProfileImageUrl = projection.opponentUserProfileImageUrl,
            ),
            myNotificationEnabled = projection.myNotificationEnabled,
            myVisible = projection.myVisible,
        )
    }
}
