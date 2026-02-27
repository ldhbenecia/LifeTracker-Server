package com.benecia.lifetracker.ledgercore.model.command

import com.benecia.lifetracker.ledgercore.service.LedgerType
import com.benecia.lifetracker.ledgercore.service.PaymentMethod
import java.time.LocalDate

data class ModifyLedgerEntry(
    val type: LedgerType?,
    val amount: Long?,
    val paymentMethod: PaymentMethod?,
    val categoryId: Long?,
    val categoryIdUpdated: Boolean,
    val memo: String?,
    val memoUpdated: Boolean,
    val transactionDate: LocalDate?,
)
