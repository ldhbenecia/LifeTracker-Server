package com.benecia.lifetracker.db.jpa.ledger

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface LedgerCategoryJpaRepository : JpaRepository<LedgerCategoryEntity, Long> {
    fun findByUserIdAndId(userId: UUID, id: Long): LedgerCategoryEntity?
    fun findAllByUserId(userId: UUID): List<LedgerCategoryEntity>
}
