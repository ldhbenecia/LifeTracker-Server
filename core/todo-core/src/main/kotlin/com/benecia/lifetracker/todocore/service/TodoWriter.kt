package com.benecia.lifetracker.todocore.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.todocore.exception.CategoryErrorCode
import com.benecia.lifetracker.todocore.exception.TodoErrorCode
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
        val categoryId = command.categoryId?.let { categoryId ->
            val exists = categoryReader.existsByUserIdAndId(userId, categoryId)
            if (!exists) throw CoreException(CategoryErrorCode.CATEGORY_NOT_FOUND)
            categoryId
        }

        val todo = Todo(
            userId = userId,
            title = command.title,
            categoryId = categoryId,
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
        val newCategoryId = command.categoryId?.let { categoryId ->
            val exists = categoryReader.existsByUserIdAndId(userId, categoryId)
            if (!exists) throw CoreException(CategoryErrorCode.CATEGORY_NOT_FOUND)
            categoryId
        } ?: existingTodo.category?.id

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
            ?: throw CoreException(TodoErrorCode.TODO_NOT_FOUND)
    }

    fun markDone(userId: UUID, id: Long, done: Boolean): Long {
        val existingTodoInfo = todoReader.findById(userId, id)

        val updatedTodo = Todo(
            id = id,
            userId = userId,
            title = existingTodoInfo.title,
            categoryId = existingTodoInfo.category?.id,
            scheduledDate = existingTodoInfo.scheduledDate,
            scheduledTime = existingTodoInfo.scheduledTime,
            notificationTime = existingTodoInfo.notificationTime,
            isDone = done,
            status = TodoStatus.ACTIVE,
        )

        return todoRepository.modify(id, updatedTodo)
            ?: throw CoreException(TodoErrorCode.TODO_NOT_FOUND)
    }

    fun remove(userId: UUID, id: Long): Long {
        return todoRepository.remove(id)
            ?: throw CoreException(TodoErrorCode.TODO_NOT_FOUND)
    }
}
