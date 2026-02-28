package com.benecia.lifetracker.db.jpa.memo

import com.benecia.lifetracker.memocore.service.MemoCategory
import com.benecia.lifetracker.memocore.service.MemoCategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class MemoCategoryEntityRepository(
    private val memoCategoryJpaRepository: MemoCategoryJpaRepository,
) : MemoCategoryRepository {

    override fun findByUserIdAndId(userId: UUID, id: Long): MemoCategory? = memoCategoryJpaRepository.findByUserIdAndId(userId, id)?.toDomain()

    override fun findAllByUserId(userId: UUID): List<MemoCategory> = memoCategoryJpaRepository.findAllByUserId(userId).map { it.toDomain() }

    override fun add(category: MemoCategory): Long = memoCategoryJpaRepository.save(MemoCategoryEntity.from(category)).id!!

    @Transactional
    override fun modify(id: Long, category: MemoCategory): Long? {
        val entity = memoCategoryJpaRepository.findByIdOrNull(id) ?: return null
        entity.name = category.name
        entity.icon = category.icon
        entity.color = category.color
        return memoCategoryJpaRepository.save(entity).id
    }

    override fun remove(id: Long): Long? {
        val entity = memoCategoryJpaRepository.findByIdOrNull(id) ?: return null
        memoCategoryJpaRepository.delete(entity)
        return id
    }
}
