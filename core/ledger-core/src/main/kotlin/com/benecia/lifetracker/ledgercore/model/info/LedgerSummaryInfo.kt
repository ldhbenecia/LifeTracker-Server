package com.benecia.lifetracker.ledgercore.model.info

import com.benecia.lifetracker.ledgercore.service.PaymentMethod

data class LedgerSummaryInfo(
    val totalIncome: Long,
    val totalExpense: Long,
    val netAmount: Long,
    val categoryBreakdown: List<CategorySummary>,
    val paymentMethodBreakdown: List<PaymentMethodSummary>,
)

data class CategorySummary(
    val categoryId: Long?,
    val totalAmount: Long,
    val count: Int,
)

data class PaymentMethodSummary(
    val paymentMethod: PaymentMethod,
    val totalAmount: Long,
    val count: Int,
)
