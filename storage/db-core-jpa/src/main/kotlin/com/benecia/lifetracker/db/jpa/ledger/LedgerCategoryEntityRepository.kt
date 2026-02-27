package com.benecia.lifetracker.db.jpa.ledger

import com.benecia.lifetracker.ledgercore.service.LedgerCategory
import com.benecia.lifetracker.ledgercore.service.LedgerCategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class LedgerCategoryEntityRepository(
    private val ledgerCategoryJpaRepository: LedgerCategoryJpaRepository,
) : LedgerCategoryRepository {

    override fun findByUserIdAndId(userId: UUID, id: Long): LedgerCategory? {
        return ledgerCategoryJpaRepository.findByUserIdAndId(userId, id)?.toDomain()
    }

    override fun findAllByUserId(userId: UUID): List<LedgerCategory> {
        return ledgerCategoryJpaRepository.findAllByUserId(userId).map { it.toDomain() }
    }

    override fun add(category: LedgerCategory): Long {
        val entity = LedgerCategoryEntity.from(category)
        return ledgerCategoryJpaRepository.save(entity).id!!
    }

    override fun modify(id: Long, category: LedgerCategory): Long? {
        val entity = ledgerCategoryJpaRepository.findByIdOrNull(id) ?: return null
        entity.name = category.name
        entity.icon = category.icon
        entity.color = category.color
        return ledgerCategoryJpaRepository.save(entity).id
    }

    override fun remove(id: Long): Long? {
        val entity = ledgerCategoryJpaRepository.findByIdOrNull(id) ?: return null
        ledgerCategoryJpaRepository.delete(entity)
        return id
    }
}
