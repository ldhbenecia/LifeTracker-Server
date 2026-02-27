package com.benecia.lifetracker.db.jpa.ledger

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface LedgerEntryJpaRepository : JpaRepository<LedgerEntryEntity, Long> {
    fun findByUserIdAndId(userId: UUID, id: Long): LedgerEntryEntity?

    @Query(
        "SELECT e FROM LedgerEntryEntity e " +
            "WHERE e.userId = :userId " +
            "AND YEAR(e.transactionDate) = :year " +
            "AND MONTH(e.transactionDate) = :month " +
            "ORDER BY e.transactionDate DESC",
    )
    fun findAllByUserIdAndYearAndMonth(
        @Param("userId") userId: UUID,
        @Param("year") year: Int,
        @Param("month") month: Int,
    ): List<LedgerEntryEntity>
}
