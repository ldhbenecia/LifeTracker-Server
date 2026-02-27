package com.benecia.lifetracker.ledgercore.exception

import com.benecia.lifetracker.common.exception.ErrorCode

enum class LedgerErrorCode(
    override val code: Int,
    override val message: String,
) : ErrorCode {
    LEDGER_ENTRY_NOT_FOUND(404, "가계부 항목을 찾을 수 없습니다."),
    LEDGER_CATEGORY_NOT_FOUND(404, "가계부 카테고리를 찾을 수 없습니다."),
    FORBIDDEN_LEDGER_ACCESS(403, "해당 가계부 항목에 접근 권한이 없습니다."),
}
