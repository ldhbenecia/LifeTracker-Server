package com.benecia.lifetracker.chat.chatRoom.service

import com.benecia.lifetracker.chat.chatRoom.exception.ChatRoomErrorCode
import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomDetail
import com.benecia.lifetracker.chat.chatRoom.model.ChatRoomSummary
import com.benecia.lifetracker.chat.chatRoom.model.UserSummary
import com.benecia.lifetracker.common.exception.CoreException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ChatRoomReader(
    private val chatRoomRepository: ChatRoomRepository,
) {

    fun readRooms(userId: UUID): List<ChatRoomSummary> {
        val roomsWithOpponents = chatRoomRepository.findRoomsWithOpponents(userId)

        return roomsWithOpponents.map { room ->
            ChatRoomSummary(
                roomId = room.roomId,
                lastMessage = room.lastMessage,
                lastMessageTime = room.lastMessageTime,
                opponent = UserSummary(
                    userId = room.opponentUserId,
                    userName = room.opponentUserName,
                    userProfileImageUrl = room.opponentProfileImageUrl,
                ),
            )
        }
    }

    fun getRoomDetail(userId: UUID, roomId: Long): ChatRoomDetail {
        return chatRoomRepository.findRoomDetail(userId, roomId)
            ?: throw CoreException(ChatRoomErrorCode.ROOM_NOT_FOUND)
    }
}
