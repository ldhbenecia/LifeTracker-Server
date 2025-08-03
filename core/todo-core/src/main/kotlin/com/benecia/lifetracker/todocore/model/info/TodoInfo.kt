package com.benecia.lifetracker.todocore.model.info

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class TodoInfo(
    val id: Long,
    val title: String,
    val category: CategoryInfo?,
    val scheduledDate: LocalDate,
    val scheduledTime: LocalTime?,
    val notificationTime: LocalDateTime?,
    val isDone: Boolean,
)
