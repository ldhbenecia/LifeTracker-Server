package com.benecia.lifetracker.todocore.model.command

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class ModifyTodo(
    val title: String? = null,
    val category: String? = null,
    val scheduledDate: LocalDate? = null,
    val scheduledTime: LocalTime? = null,
    val notificationTime: LocalDateTime? = null,
    val isDone: Boolean? = null,
)
