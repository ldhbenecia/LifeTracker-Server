package com.benecia.lifetracker.domain.todo.dto

import com.benecia.lifetracker.todocore.model.command.NewTodo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class NewTodoRequest(
    val title: String,
    val category: String,
    val scheduledDate: LocalDate,
    val scheduledTime: LocalTime? = null,
    val notificationTime: LocalDateTime? = null,
) {
    fun toNewTodo(): NewTodo {
        return NewTodo(
            title = this.title,
            category = this.category,
            scheduledDate = this.scheduledDate,
            scheduledTime = this.scheduledTime,
            notificationTime = this.notificationTime,
        )
    }
}
