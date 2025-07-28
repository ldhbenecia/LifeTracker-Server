package com.benecia.lifetracker.db.jpa.todo

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID

interface TodoJpaRepository : JpaRepository<TodoEntity, Long> {
    fun findByUserIdAndId(userId: UUID, id: Long): TodoEntity?
    fun findByUserIdAndScheduledDateBetween(
        userId: UUID,
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<TodoEntity>
}
