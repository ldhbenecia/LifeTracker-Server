package com.benecia.lifetracker.db.jpa.memo

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MemoJpaRepository : JpaRepository<MemoEntity, Long> {
    fun findByUserIdAndId(userId: UUID, id: Long): MemoEntity?

    fun findAllByUserIdOrderByCreatedAtDesc(userId: UUID): List<MemoEntity>
}
