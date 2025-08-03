package com.benecia.lifetracker.domain.todo.dto

import com.benecia.lifetracker.todocore.model.info.TodoInfo
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val category: CategoryResponse?,
    val scheduledDate: LocalDate,
    val scheduledTime: LocalTime?,
    val notificationTime: LocalDateTime?,

    @JsonProperty(value = "isDone")
    val isDone: Boolean,
) {
    companion object {
        fun of(info: TodoInfo): TodoResponse = TodoResponse(
            id = info.id,
            title = info.title,
            category = info.category?.let { CategoryResponse.of(it) },
            scheduledDate = info.scheduledDate,
            scheduledTime = info.scheduledTime,
            notificationTime = info.notificationTime,
            isDone = info.isDone,
        )
    }
}
