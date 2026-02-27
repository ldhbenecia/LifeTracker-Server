package com.benecia.lifetracker.ledgercore.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.ledgercore.exception.LedgerErrorCode
import com.benecia.lifetracker.ledgercore.model.command.AddLedgerEntry
import com.benecia.lifetracker.ledgercore.model.command.ModifyLedgerEntry
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class LedgerEntryWriter(
    private val ledgerEntryReader: LedgerEntryReader,
    private val ledgerEntryRepository: LedgerEntryRepository,
) {
    fun add(userId: UUID, command: AddLedgerEntry): Long {
        val entry = LedgerEntry(
            userId = userId,
            type = command.type,
            amount = command.amount,
            paymentMethod = command.paymentMethod,
            categoryId = command.categoryId,
            memo = command.memo,
            transactionDate = command.transactionDate,
        )
        return ledgerEntryRepository.add(entry)
    }

    fun modify(userId: UUID, id: Long, command: ModifyLedgerEntry): Long {
        val existing = ledgerEntryReader.findByUserIdAndId(userId, id)
        val updated = LedgerEntry(
            id = existing.id,
            userId = userId,
            type = command.type ?: existing.type,
            amount = command.amount ?: existing.amount,
            paymentMethod = command.paymentMethod ?: existing.paymentMethod,
            categoryId = if (command.categoryIdUpdated) command.categoryId else existing.categoryId,
            memo = if (command.memoUpdated) command.memo else existing.memo,
            transactionDate = command.transactionDate ?: existing.transactionDate,
        )
        return ledgerEntryRepository.modify(id, updated)
            ?: throw CoreException(LedgerErrorCode.LEDGER_ENTRY_NOT_FOUND)
    }

    fun remove(userId: UUID, id: Long): Long {
        ledgerEntryReader.findByUserIdAndId(userId, id)
        return ledgerEntryRepository.remove(id)
            ?: throw CoreException(LedgerErrorCode.LEDGER_ENTRY_NOT_FOUND)
    }
}
