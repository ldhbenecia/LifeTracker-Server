package com.benecia.lifetracker.todocore.model.command

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class NewTodo(
    val title: String,
    val category: String,
    val scheduledDate: LocalDate,
    val scheduledTime: LocalTime? = null,
    val notificationTime: LocalDateTime? = null,
)
