package com.benecia.lifetracker.db.jpa.chat

import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomDetail
import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomWithOpponent
import com.benecia.lifetracker.chat.chatRoom.model.UserSummary
import com.benecia.lifetracker.chat.chatRoom.service.ChatRoom
import com.benecia.lifetracker.chat.chatRoom.service.ChatRoomRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Repository
class ChatRoomEntityRepository(
    private val chatRoomJpaRepository: ChatRoomJpaRepository,
    private val chatRoomUserJpaRepository: ChatRoomUserJpaRepository,
) : ChatRoomRepository {

    override fun findRoomIdByUserIds(user1Id: UUID, user2Id: UUID): Long? {
        return chatRoomUserJpaRepository.findRoomIdByUserIds(user1Id, user2Id)
    }

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
        val chatRoom = chatRoomJpaRepository.save(ChatRoomEntity())
        val roomId = chatRoom.id!!

        val user1 = ChatRoomUserEntity(id = ChatRoomUserId(roomId = roomId, userId = userId))
        val user2 = ChatRoomUserEntity(id = ChatRoomUserId(roomId = roomId, userId = opponentUserId))

        chatRoomUserJpaRepository.saveAll(listOf(user1, user2))
        return roomId
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

    @Transactional
    override fun updateLastMessage(roomId: Long, message: String, messageTime: LocalDateTime) {
        chatRoomJpaRepository.findByIdOrNull(roomId)?.let {
            it.lastMessage = message
            it.lastMessageTime = messageTime
        }
    }
}
