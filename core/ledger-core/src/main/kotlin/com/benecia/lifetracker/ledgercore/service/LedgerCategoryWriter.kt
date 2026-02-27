package com.benecia.lifetracker.ledgercore.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.ledgercore.exception.LedgerErrorCode
import com.benecia.lifetracker.ledgercore.model.command.AddLedgerCategory
import com.benecia.lifetracker.ledgercore.model.command.ModifyLedgerCategory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class LedgerCategoryWriter(
    private val ledgerCategoryReader: LedgerCategoryReader,
    private val ledgerCategoryRepository: LedgerCategoryRepository,
) {
    fun add(userId: UUID, command: AddLedgerCategory): Long {
        val category = LedgerCategory(
            userId = userId,
            name = command.name,
            icon = command.icon,
            color = command.color,
        )
        return ledgerCategoryRepository.add(category)
    }

    fun modify(userId: UUID, id: Long, command: ModifyLedgerCategory): Long {
        val existing = ledgerCategoryReader.findByUserIdAndId(userId, id)
        val updated = LedgerCategory(
            id = existing.id,
            userId = userId,
            name = command.name ?: existing.name,
            icon = command.icon ?: existing.icon,
            color = command.color ?: existing.color,
        )
        return ledgerCategoryRepository.modify(id, updated)
            ?: throw CoreException(LedgerErrorCode.LEDGER_CATEGORY_NOT_FOUND)
    }

    fun remove(userId: UUID, id: Long): Long {
        ledgerCategoryReader.findByUserIdAndId(userId, id)
        return ledgerCategoryRepository.remove(id)
            ?: throw CoreException(LedgerErrorCode.LEDGER_CATEGORY_NOT_FOUND)
    }
}
