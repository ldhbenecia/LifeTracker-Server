package com.benecia.lifetracker.db.jpa.todo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

interface TodoJpaRepository : JpaRepository<TodoEntity, Long> {
    fun findByUserIdAndId(userId: UUID, id: Long): TodoEntity?
    fun findByUserIdAndScheduledDateBetween(userId: UUID, start: LocalDate, end: LocalDate): List<TodoEntity>
    fun findAllByNotificationTimeBetween(start: LocalDateTime, end: LocalDateTime): List<TodoEntity>

    @Modifying
    @Query("UPDATE TodoEntity t SET t.categoryId = null WHERE t.categoryId = :categoryId")
    fun nullifyCategoryIdByCategoryId(categoryId: Long): Int
}
