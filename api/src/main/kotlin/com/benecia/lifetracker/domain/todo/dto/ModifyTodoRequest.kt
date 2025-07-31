package com.benecia.lifetracker.domain.todo.dto

import com.benecia.lifetracker.todocore.model.command.ModifyTodo
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class ModifyTodoRequest(
    val title: String? = null,
    val category: String? = null,
    val scheduledDate: LocalDate? = null,
    val scheduledTime: LocalTime? = null,
    val notificationTime: LocalDateTime? = null,

    @JsonProperty(value = "isDone")
    val isDone: Boolean? = null,
) {
    fun toModifyTodo(): ModifyTodo {
        return ModifyTodo(
            title = this.title,
            category = this.category,
            scheduledDate = this.scheduledDate,
            scheduledTime = this.scheduledTime,
            notificationTime = this.notificationTime,
            isDone = this.isDone,
        )
    }
}
