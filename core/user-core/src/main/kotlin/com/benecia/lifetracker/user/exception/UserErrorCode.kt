package com.benecia.lifetracker.user.exception

import com.benecia.lifetracker.common.exception.ErrorCode

enum class UserErrorCode(
    override val code: Int,
    override val message: String,
) : ErrorCode {
    USER_NOT_FOUND(404, "해당 ID의 사용자를 찾을 수 없습니다."),
    INVALID_ACCESS_TOKEN(401, "유효하지 않은 액세스 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(401, "만료된 액세스 토큰입니다."),
    FORBIDDEN_USER_ACCESS(403, "해당 리소스에 접근할 권한이 없습니다."),
    LOGGED_OUT_TOKEN(401, "이미 로그아웃된 토큰입니다."),
}
