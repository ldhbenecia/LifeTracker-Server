package com.benecia.lifetracker.chat.chatRoom.exception

import com.benecia.lifetracker.common.exception.ErrorCode

enum class ChatRoomErrorCode(
    override val code: Int,
    override val message: String,
): ErrorCode {
    CHAT_ROOM_USER_NOT_FOUND(404, "채팅방에서 사용자를 찾을 수 없습니다.")
}