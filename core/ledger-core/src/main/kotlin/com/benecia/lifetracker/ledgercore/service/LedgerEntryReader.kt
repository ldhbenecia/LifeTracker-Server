package com.benecia.lifetracker.ledgercore.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.ledgercore.exception.LedgerErrorCode
import com.benecia.lifetracker.ledgercore.model.info.LedgerEntryInfo
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class LedgerEntryReader(
    private val ledgerEntryRepository: LedgerEntryRepository,
) {
    fun findByUserIdAndId(userId: UUID, id: Long): LedgerEntryInfo {
        val entry = ledgerEntryRepository.findByUserIdAndId(userId, id)
            ?: throw CoreException(LedgerErrorCode.LEDGER_ENTRY_NOT_FOUND)
        return entry.toInfo()
    }

    fun findAllByUserIdAndYearMonth(userId: UUID, year: Int, month: Int): List<LedgerEntryInfo> {
        return ledgerEntryRepository.findAllByUserIdAndYearMonth(userId, year, month)
            .map { it.toInfo() }
    }

    private fun LedgerEntry.toInfo() = LedgerEntryInfo(
        id = this.id!!,
        type = this.type,
        amount = this.amount,
        paymentMethod = this.paymentMethod,
        categoryId = this.categoryId,
        memo = this.memo,
        transactionDate = this.transactionDate,
    )
}
