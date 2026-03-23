package com.benecia.lifetracker.todocore.service

import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface TodoRepository {
    fun findByUserIdAndId(userId: UUID, id: Long): Todo?
    fun findByUserIdAndScheduledDateRange(userId: UUID, start: LocalDate, end: LocalDate): List<Todo>
    fun add(todo: Todo): Long
    fun modify(id: Long, todo: Todo): Long?
    fun remove(id: Long): Long?
    fun findAllByNotificationTimeBetween(start: LocalDateTime, end: LocalDateTime): List<Todo>
}
