package com.benecia.lifetracker.ledgercore.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LedgerCategoryRepository {
    fun findByUserIdAndId(userId: UUID, id: Long): LedgerCategory?
    fun findAllByUserId(userId: UUID): List<LedgerCategory>
    fun add(category: LedgerCategory): Long
    fun modify(id: Long, category: LedgerCategory): Long?
    fun remove(id: Long): Long?
}
