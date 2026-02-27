package com.benecia.lifetracker.db.jpa.ledger

import com.benecia.lifetracker.ledgercore.service.LedgerEntry
import com.benecia.lifetracker.ledgercore.service.LedgerEntryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class LedgerEntryEntityRepository(
    private val ledgerEntryJpaRepository: LedgerEntryJpaRepository,
) : LedgerEntryRepository {

    override fun findByUserIdAndId(userId: UUID, id: Long): LedgerEntry? {
        return ledgerEntryJpaRepository.findByUserIdAndId(userId, id)?.toDomain()
    }

    override fun findAllByUserIdAndYearMonth(userId: UUID, year: Int, month: Int): List<LedgerEntry> {
        return ledgerEntryJpaRepository.findAllByUserIdAndYearAndMonth(userId, year, month)
            .map { it.toDomain() }
    }

    override fun add(entry: LedgerEntry): Long {
        val entity = LedgerEntryEntity.from(entry)
        return ledgerEntryJpaRepository.save(entity).id!!
    }

    override fun modify(id: Long, entry: LedgerEntry): Long? {
        val entity = ledgerEntryJpaRepository.findByIdOrNull(id) ?: return null
        entity.type = entry.type
        entity.amount = entry.amount
        entity.paymentMethod = entry.paymentMethod
        entity.categoryId = entry.categoryId
        entity.memo = entry.memo
        entity.transactionDate = entry.transactionDate
        return ledgerEntryJpaRepository.save(entity).id
    }

    override fun remove(id: Long): Long? {
        val entity = ledgerEntryJpaRepository.findByIdOrNull(id) ?: return null
        ledgerEntryJpaRepository.delete(entity)
        return id
    }
}
