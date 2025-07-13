package com.benecia.lifetracker.domain.todo.dto

import com.benecia.lifetracker.todocore.model.info.TodoInfo
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val category: CategoryResponse,
    val scheduledDate: LocalDateTime,
    val notificationTime: LocalDateTime?,

    @JsonProperty(value = "isDone")
    val isDone: Boolean,
) {
    companion object {
        fun of(info: TodoInfo): TodoResponse = TodoResponse(
            id = info.id,
            title = info.title,
            category = CategoryResponse.of(info.category),
            scheduledDate = info.scheduledDate,
            notificationTime = info.notificationTime,
            isDone = info.isDone,
        )
    }
}
