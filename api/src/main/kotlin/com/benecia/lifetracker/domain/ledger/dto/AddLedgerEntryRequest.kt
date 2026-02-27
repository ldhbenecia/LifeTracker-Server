package com.benecia.lifetracker.domain.ledger.dto

import com.benecia.lifetracker.ledgercore.model.command.AddLedgerEntry
import com.benecia.lifetracker.ledgercore.service.LedgerType
import com.benecia.lifetracker.ledgercore.service.PaymentMethod
import java.time.LocalDate

data class AddLedgerEntryRequest(
    val type: LedgerType,
    val amount: Long,
    val paymentMethod: PaymentMethod,
    val categoryId: Long?,
    val memo: String?,
    val transactionDate: LocalDate,
) {
    fun toCommand(): AddLedgerEntry = AddLedgerEntry(
        type = type,
        amount = amount,
        paymentMethod = paymentMethod,
        categoryId = categoryId,
        memo = memo,
        transactionDate = transactionDate,
    )
}
