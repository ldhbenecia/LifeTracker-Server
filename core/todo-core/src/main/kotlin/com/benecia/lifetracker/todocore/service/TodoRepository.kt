package com.benecia.lifetracker.todocore.service

import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface TodoRepository {
    fun findByUserIdAndId(userId: UUID, id: Long): Todo
    fun findByUserIdAndScheduledDateRange(
        userId: UUID,
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<Todo>
    fun add(todo: Todo): Long
    fun modify(id: Long, todo: Todo): Long
}
