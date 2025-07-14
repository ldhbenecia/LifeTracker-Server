package com.benecia.lifetracker.todocore.service

import com.benecia.lifetracker.todocore.model.info.TodoInfo
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
data class TodoReader(
    private val categoryReader: CategoryReader,
    private val todoRepository: TodoRepository,
) {
    fun findById(userId: UUID, id: Long): TodoInfo {
        val todo = todoRepository.findByUserIdAndId(userId, id)
        val category = categoryReader.findByUserIdAndId(userId, todo.categoryId)

        return TodoInfo(
            id = id,
            title = todo.title,
            category = category,
            scheduledDate = todo.scheduledDate,
            notificationTime = todo.notificationTime,
            isDone = todo.isDone,
        )
    }

    fun findTodosByMonth(
        userId: UUID,
        year: Int,
        month: Int,
        page: Int,
        size: Int,
    ): List<TodoInfo> {
        val start = LocalDateTime.of(year, month, 1, 0, 0)
        val end = start.withDayOfMonth(start.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59)

        val todos = todoRepository.findByUserIdAndScheduledDateRange(userId, start, end, page, size)
        return todos.map { todo ->
            TodoInfo(
                id = todo.id!!,
                title = todo.title,
                category = categoryReader.findByUserIdAndId(userId, todo.categoryId),
                scheduledDate = todo.scheduledDate,
                notificationTime = todo.notificationTime,
                isDone = todo.isDone,
            )
        }
    }
}
