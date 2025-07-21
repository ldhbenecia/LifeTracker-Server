package com.benecia.lifetracker.todocore.service

import com.benecia.lifetracker.todocore.model.command.ModifyTodo
import com.benecia.lifetracker.todocore.model.command.NewTodo
import org.springframework.stereotype.Component
import java.util.UUID

@Component
data class TodoWriter(
    private val todoReader: TodoReader,
    private val categoryReader: CategoryReader,
    private val todoRepository: TodoRepository,
) {
    fun add(userId: UUID, command: NewTodo): Long {
        val category = categoryReader.findByUserIdAndName(userId, command.category)

        val todo = Todo(
            userId = userId,
            title = command.title,
            categoryId = category.id,
            scheduledDate = command.scheduledDate,
            notificationTime = command.notificationTime,
            isDone = false,
        )

        return todoRepository.add(todo)
    }

    fun modify(userId: UUID, id: Long, command: ModifyTodo): Long {
        val existingTodo = todoReader.findById(userId, id)

        // category가 변경되었으면 새 categoryId 조회, 아니면 기존 categoryId 유지
        val newCategoryId = command.category?.let { categoryName ->
            categoryReader.findByUserIdAndName(userId, categoryName).id
        } ?: existingTodo.category.id

        val modifiedTodo = Todo(
            id = id,
            userId = userId,
            title = command.title ?: existingTodo.title,
            categoryId = newCategoryId,
            scheduledDate = command.scheduledDate ?: existingTodo.scheduledDate,
            notificationTime = command.notificationTime ?: existingTodo.notificationTime,
            isDone = command.isDone ?: existingTodo.isDone,
        )

        return todoRepository.modify(id, modifiedTodo)
    }
}
