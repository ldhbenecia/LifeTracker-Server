package com.benecia.lifetracker.todocore.service

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

data class Todo(
    val id: Long? = null,
    val userId: UUID,
    val categoryId: Long,
    val title: String,
    val scheduledDate: LocalDate,
    val scheduledTime: LocalTime?,
    val notificationTime: LocalDateTime?,
    val isDone: Boolean,
    val status: TodoStatus = TodoStatus.ACTIVE,
)
