package com.benecia.lifetracker.db.jpa.memo

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MemoCategoryJpaRepository : JpaRepository<MemoCategoryEntity, Long> {
    fun findByUserIdAndId(userId: UUID, id: Long): MemoCategoryEntity?

    fun findAllByUserId(userId: UUID): List<MemoCategoryEntity>
}
