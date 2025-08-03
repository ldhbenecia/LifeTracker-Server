package com.benecia.lifetracker.todocore.exception

import com.benecia.lifetracker.common.exception.ErrorCode

enum class CategoryErrorCode(
    override val code: Int,
    override val message: String,
) : ErrorCode {
    CATEGORY_NOT_FOUND(404, "해당 ID의 카테고리를 찾을 수 없습니다."),
    CATEGORY_PERSIST_FAILED(409, "카테고리 저장에 실패했습니다."),
    CATEGORY_NAME_TOO_LONG(400, "카테고리 이름은 10자 이하로 입력해야 합니다."),
}
