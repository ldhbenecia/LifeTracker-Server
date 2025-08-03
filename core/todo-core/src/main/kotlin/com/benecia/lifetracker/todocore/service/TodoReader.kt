package com.benecia.lifetracker.todocore.service

import com.benecia.lifetracker.todocore.model.info.TodoInfo
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID

@Component
data class TodoReader(
    private val categoryReader: CategoryReader,
    private val todoRepository: TodoRepository,
) {
    fun findById(userId: UUID, id: Long): TodoInfo {
        val todo = todoRepository.findByUserIdAndId(userId, id)
        val category = todo.categoryId?.let { categoryReader.findByUserIdAndId(userId, todo.categoryId) }

        return TodoInfo(
            id = id,
            title = todo.title,
            category = category,
            scheduledDate = todo.scheduledDate,
            scheduledTime = todo.scheduledTime,
            notificationTime = todo.notificationTime,
            isDone = todo.isDone,
        )
    }

    fun findTodosByMonth(
        userId: UUID,
        year: Int,
        month: Int,
    ): List<TodoInfo> {
        val start = LocalDate.of(year, month, 1)
        val end = start.withDayOfMonth(start.lengthOfMonth())

        val todos = todoRepository.findByUserIdAndScheduledDateRange(userId, start, end)

        // 카테고리 아이디가 null이 아닌 것만 조회해서 카테고리 정보만 미리 가져옴
        val categoryIds = todos.mapNotNull { it.categoryId }.distinct()
        val categories = categoryReader.findByUserIdAndIds(userId, categoryIds)
        val categoryMap = categories.associateBy { it.id }

        return todos.map { todo ->
            val category = todo.categoryId?.let { categoryMap[it] }
            TodoInfo(
                id = todo.id!!,
                title = todo.title,
                category = category,
                scheduledDate = todo.scheduledDate,
                scheduledTime = todo.scheduledTime,
                notificationTime = todo.notificationTime,
                isDone = todo.isDone,
            )
        }
    }
}
