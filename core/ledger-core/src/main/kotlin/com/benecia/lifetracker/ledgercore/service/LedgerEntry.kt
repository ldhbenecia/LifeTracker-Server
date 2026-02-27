package com.benecia.lifetracker.ledgercore.service

import java.time.LocalDate
import java.util.UUID

data class LedgerEntry(
    val id: Long? = null,
    val userId: UUID,
    val type: LedgerType,
    val amount: Long,
    val paymentMethod: PaymentMethod,
    val categoryId: Long?,
    val memo: String?,
    val transactionDate: LocalDate,
)
