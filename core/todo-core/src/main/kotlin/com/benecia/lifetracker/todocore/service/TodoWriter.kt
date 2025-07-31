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
            scheduledTime = command.scheduledTime,
            notificationTime = command.notificationTime,
            isDone = false,
            status = TodoStatus.ACTIVE,
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
            scheduledTime = command.scheduledTime ?: existingTodo.scheduledTime,
            notificationTime = command.notificationTime ?: existingTodo.notificationTime,
            isDone = command.isDone ?: existingTodo.isDone,
            status = TodoStatus.ACTIVE,
        )

        return todoRepository.modify(id, modifiedTodo)
    }

    fun markDone(userId: UUID, id: Long, done: Boolean): Long {
        val existingTodoInfo = todoReader.findById(userId, id)

        val updatedTodo = Todo(
            id = id,
            userId = userId,
            title = existingTodoInfo.title,
            categoryId = existingTodoInfo.category.id,
            scheduledDate = existingTodoInfo.scheduledDate,
            scheduledTime = existingTodoInfo.scheduledTime,
            notificationTime = existingTodoInfo.notificationTime,
            isDone = done,
            status = TodoStatus.ACTIVE,
        )

        return todoRepository.modify(id, updatedTodo)
    }

    fun remove(userId: UUID, id: Long): Long {
        return todoRepository.remove(id)
    }
}
