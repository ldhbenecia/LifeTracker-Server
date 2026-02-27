package com.benecia.lifetracker.ledgercore.model.info

import com.benecia.lifetracker.ledgercore.service.LedgerType
import com.benecia.lifetracker.ledgercore.service.PaymentMethod
import java.time.LocalDate

data class LedgerEntryInfo(
    val id: Long,
    val type: LedgerType,
    val amount: Long,
    val paymentMethod: PaymentMethod,
    val categoryId: Long?,
    val memo: String?,
    val transactionDate: LocalDate,
)
