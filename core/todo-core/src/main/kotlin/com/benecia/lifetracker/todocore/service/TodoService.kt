package com.benecia.lifetracker.todocore.service

import com.benecia.lifetracker.todocore.model.command.ModifyTodo
import com.benecia.lifetracker.todocore.model.command.NewTodo
import com.benecia.lifetracker.todocore.model.info.TodoInfo
import com.benecia.lifetracker.todocore.model.info.TodoStatisticsInfo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TodoService(
    private val todoWriter: TodoWriter,
    private val todoReader: TodoReader,
) {
    fun findTodoById(userId: UUID, id: Long): TodoInfo {
        return todoReader.findById(userId, id)
    }

    fun findTodosByMonth(
        userId: UUID,
        year: Int,
        month: Int,
    ): List<TodoInfo> {
        return todoReader.findTodosByMonth(userId, year, month)
    }

    fun addTodo(userId: UUID, command: NewTodo): Long {
        return todoWriter.add(userId, command)
    }

    fun modifyTodo(userId: UUID, id: Long, command: ModifyTodo): Long {
        return todoWriter.modify(userId, id, command)
    }

    fun markDone(userId: UUID, id: Long, done: Boolean): Long {
        return todoWriter.markDone(userId, id, done)
    }

    fun removeTodo(userId: UUID, id: Long): Long {
        return todoWriter.remove(userId, id)
    }

    fun getMonthlyStatistics(
        userId: UUID,
        year: Int,
        month: Int,
    ): TodoStatisticsInfo {
        return todoReader.getMonthlyStatistics(userId, year, month)
    }
}
