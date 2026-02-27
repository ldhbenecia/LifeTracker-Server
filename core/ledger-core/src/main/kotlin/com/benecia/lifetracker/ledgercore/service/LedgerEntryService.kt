package com.benecia.lifetracker.ledgercore.service

import com.benecia.lifetracker.ledgercore.model.command.AddLedgerEntry
import com.benecia.lifetracker.ledgercore.model.command.ModifyLedgerEntry
import com.benecia.lifetracker.ledgercore.model.info.CategorySummary
import com.benecia.lifetracker.ledgercore.model.info.LedgerEntryInfo
import com.benecia.lifetracker.ledgercore.model.info.LedgerSummaryInfo
import com.benecia.lifetracker.ledgercore.model.info.PaymentMethodSummary
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LedgerEntryService(
    private val ledgerEntryReader: LedgerEntryReader,
    private val ledgerEntryWriter: LedgerEntryWriter,
) {
    fun findAllByUserIdAndYearMonth(userId: UUID, year: Int, month: Int): List<LedgerEntryInfo> {
        return ledgerEntryReader.findAllByUserIdAndYearMonth(userId, year, month)
    }

    fun getSummary(userId: UUID, year: Int, month: Int): LedgerSummaryInfo {
        val entries = ledgerEntryReader.findAllByUserIdAndYearMonth(userId, year, month)

        val totalIncome = entries.filter { it.type == LedgerType.INCOME }.sumOf { it.amount }
        val totalExpense = entries.filter { it.type == LedgerType.EXPENSE }.sumOf { it.amount }

        val categoryBreakdown = entries
            .groupBy { it.categoryId }
            .map { (categoryId, group) ->
                CategorySummary(
                    categoryId = categoryId,
                    totalAmount = group.sumOf { it.amount },
                    count = group.size,
                )
            }

        val paymentMethodBreakdown = entries
            .groupBy { it.paymentMethod }
            .map { (paymentMethod, group) ->
                PaymentMethodSummary(
                    paymentMethod = paymentMethod,
                    totalAmount = group.sumOf { it.amount },
                    count = group.size,
                )
            }

        return LedgerSummaryInfo(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            netAmount = totalIncome - totalExpense,
            categoryBreakdown = categoryBreakdown,
            paymentMethodBreakdown = paymentMethodBreakdown,
        )
    }

    fun add(userId: UUID, command: AddLedgerEntry): Long {
        return ledgerEntryWriter.add(userId, command)
    }

    fun modify(userId: UUID, id: Long, command: ModifyLedgerEntry): Long {
        return ledgerEntryWriter.modify(userId, id, command)
    }

    fun remove(userId: UUID, id: Long): Long {
        return ledgerEntryWriter.remove(userId, id)
    }
}
