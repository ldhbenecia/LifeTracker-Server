package com.benecia.lifetracker.domain.ledger.dto

import com.benecia.lifetracker.ledgercore.model.command.ModifyLedgerEntry
import com.benecia.lifetracker.ledgercore.service.LedgerType
import com.benecia.lifetracker.ledgercore.service.PaymentMethod
import java.time.LocalDate

data class ModifyLedgerEntryRequest(
    val type: LedgerType?,
    val amount: Long?,
    val paymentMethod: PaymentMethod?,
    val categoryId: Long?,
    val categoryIdUpdated: Boolean = false,
    val memo: String?,
    val memoUpdated: Boolean = false,
    val transactionDate: LocalDate?,
) {
    fun toCommand(): ModifyLedgerEntry = ModifyLedgerEntry(
        type = type,
        amount = amount,
        paymentMethod = paymentMethod,
        categoryId = categoryId,
        categoryIdUpdated = categoryIdUpdated,
        memo = memo,
        memoUpdated = memoUpdated,
        transactionDate = transactionDate,
    )
}
