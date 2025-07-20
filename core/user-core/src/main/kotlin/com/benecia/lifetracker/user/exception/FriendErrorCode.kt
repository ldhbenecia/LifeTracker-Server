package com.benecia.lifetracker.user.exception

import com.benecia.lifetracker.common.exception.ErrorCode

enum class FriendErrorCode(
    override val code: Int,
    override val message: String,
) : ErrorCode {
    ALREADY_REQUESTED(403, "이미 친구 요청을 보냈거나 친구 상태입니다."),
    FRIEND_REQUEST_NOT_FOUND(404, "친구 추가 요청을 찾을 수 없습니다."),
}
