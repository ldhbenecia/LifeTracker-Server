package com.benecia.lifetracker.ledgercore.service

import com.benecia.lifetracker.ledgercore.model.command.AddLedgerCategory
import com.benecia.lifetracker.ledgercore.model.command.ModifyLedgerCategory
import com.benecia.lifetracker.ledgercore.model.info.LedgerCategoryInfo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LedgerCategoryService(
    private val ledgerCategoryReader: LedgerCategoryReader,
    private val ledgerCategoryWriter: LedgerCategoryWriter,
) {
    fun findAllByUserId(userId: UUID): List<LedgerCategoryInfo> {
        return ledgerCategoryReader.findAllByUserId(userId)
    }

    fun add(userId: UUID, command: AddLedgerCategory): Long {
        return ledgerCategoryWriter.add(userId, command)
    }

    fun modify(userId: UUID, id: Long, command: ModifyLedgerCategory): Long {
        return ledgerCategoryWriter.modify(userId, id, command)
    }

    fun remove(userId: UUID, id: Long): Long {
        return ledgerCategoryWriter.remove(userId, id)
    }
}
