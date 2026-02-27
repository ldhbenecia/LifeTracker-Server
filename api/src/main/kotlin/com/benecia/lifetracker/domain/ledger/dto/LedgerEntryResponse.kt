package com.benecia.lifetracker.domain.ledger.dto

import com.benecia.lifetracker.ledgercore.model.info.LedgerEntryInfo
import com.benecia.lifetracker.ledgercore.service.LedgerType
import com.benecia.lifetracker.ledgercore.service.PaymentMethod
import java.time.LocalDate

data class LedgerEntryResponse(
    val id: Long,
    val type: LedgerType,
    val amount: Long,
    val paymentMethod: PaymentMethod,
    val categoryId: Long?,
    val memo: String?,
    val transactionDate: LocalDate,
) {
    companion object {
        fun of(info: LedgerEntryInfo): LedgerEntryResponse = LedgerEntryResponse(
            id = info.id,
            type = info.type,
            amount = info.amount,
            paymentMethod = info.paymentMethod,
            categoryId = info.categoryId,
            memo = info.memo,
            transactionDate = info.transactionDate,
        )
    }
}
