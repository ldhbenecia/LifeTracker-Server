package com.benecia.lifetracker.memocore.exception

import com.benecia.lifetracker.common.exception.ErrorCode

enum class MemoErrorCode(
    override val code: Int,
    override val message: String,
) : ErrorCode {
    MEMO_NOT_FOUND(404, "메모를 찾을 수 없습니다."),
    MEMO_CATEGORY_NOT_FOUND(404, "메모 카테고리를 찾을 수 없습니다."),
}
