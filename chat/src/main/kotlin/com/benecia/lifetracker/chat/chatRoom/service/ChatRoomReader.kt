package com.benecia.lifetracker.chat.chatRoom.service

import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomSummary
import com.benecia.lifetracker.chat.chatRoom.model.UserSummary
import com.benecia.lifetracker.user.service.UserReader
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ChatRoomReader(
    private val userReader: UserReader,
    private val chatRoomRepository: ChatRoomRepository
) {

    fun readRooms(userId: UUID): List<ChatRoomSummary> {
        val rooms = chatRoomRepository.findAllRoomsByUserId(userId)

        return rooms.map { room ->
            val opponentUserId = chatRoomRepository.findOpponentUserId(userId, room.id!!)
            val userInfo = userReader.findById(opponentUserId)
            val opponentUser = UserSummary(
                userId = userInfo.id,
                userName = userInfo.displayName,
                userProfileImageUrl = userInfo.profileImageUrl,
            )
            ChatRoomSummary(
                roomId = room.id.toString(),
                lastMessage = room.lastMessage,
                lastMessageTime = room.lastMessageTime,
                opponent = opponentUser
            )
        }
    }
}