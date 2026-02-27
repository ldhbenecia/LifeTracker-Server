package com.benecia.lifetracker.ledgercore.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.ledgercore.exception.LedgerErrorCode
import com.benecia.lifetracker.ledgercore.model.info.LedgerCategoryInfo
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class LedgerCategoryReader(
    private val ledgerCategoryRepository: LedgerCategoryRepository,
) {
    fun findByUserIdAndId(userId: UUID, id: Long): LedgerCategoryInfo {
        val category = ledgerCategoryRepository.findByUserIdAndId(userId, id)
            ?: throw CoreException(LedgerErrorCode.LEDGER_CATEGORY_NOT_FOUND)
        return LedgerCategoryInfo(
            id = category.id!!,
            name = category.name,
            icon = category.icon,
            color = category.color,
        )
    }

    fun findAllByUserId(userId: UUID): List<LedgerCategoryInfo> {
        return ledgerCategoryRepository.findAllByUserId(userId).map { category ->
            LedgerCategoryInfo(
                id = category.id!!,
                name = category.name,
                icon = category.icon,
                color = category.color,
            )
        }
    }
}
