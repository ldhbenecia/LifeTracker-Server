package com.benecia.lifetracker.domain.ledger.dto

import com.benecia.lifetracker.ledgercore.model.info.LedgerSummaryInfo
import com.benecia.lifetracker.ledgercore.service.PaymentMethod

data class LedgerSummaryResponse(
    val totalIncome: Long,
    val totalExpense: Long,
    val netAmount: Long,
    val categoryBreakdown: List<CategorySummaryResponse>,
    val paymentMethodBreakdown: List<PaymentMethodSummaryResponse>,
) {
    companion object {
        fun of(info: LedgerSummaryInfo): LedgerSummaryResponse = LedgerSummaryResponse(
            totalIncome = info.totalIncome,
            totalExpense = info.totalExpense,
            netAmount = info.netAmount,
            categoryBreakdown = info.categoryBreakdown.map {
                CategorySummaryResponse(
                    categoryId = it.categoryId,
                    totalAmount = it.totalAmount,
                    count = it.count,
                )
            },
            paymentMethodBreakdown = info.paymentMethodBreakdown.map {
                PaymentMethodSummaryResponse(
                    paymentMethod = it.paymentMethod,
                    totalAmount = it.totalAmount,
                    count = it.count,
                )
            },
        )
    }
}

data class CategorySummaryResponse(
    val categoryId: Long?,
    val totalAmount: Long,
    val count: Int,
)

data class PaymentMethodSummaryResponse(
    val paymentMethod: PaymentMethod,
    val totalAmount: Long,
    val count: Int,
)
