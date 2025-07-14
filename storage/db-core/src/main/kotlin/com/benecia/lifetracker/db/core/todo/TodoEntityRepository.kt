package com.benecia.lifetracker.db.core.todo

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.todocore.exception.TodoErrorCode
import com.benecia.lifetracker.todocore.service.Todo
import com.benecia.lifetracker.todocore.service.TodoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Repository
class TodoEntityRepository(
    private val todoJpaRepository: TodoJpaRepository,
) : TodoRepository {

    override fun findByUserIdAndId(userId: UUID, id: Long): Todo {
        val entity = todoJpaRepository.findByUserIdAndId(userId, id)
            ?: throw CoreException(TodoErrorCode.TODO_NOT_FOUND)
        return entity.toDomain()
    }

    override fun findByUserIdAndScheduledDateRange(
        userId: UUID,
        start: LocalDateTime,
        end: LocalDateTime,
        page: Int,
        size: Int,
    ): List<Todo> {
        val pageable = PageRequest.of(page, size)
        val entities = todoJpaRepository.findByUserIdAndScheduledDateBetween(userId, start, end, pageable)
        return entities.content.map { it.toDomain() }
    }

    override fun add(todo: Todo): Long {
        val entity = TodoEntity.from(todo)
        return todoJpaRepository.save(entity).id!!
    }

    @Transactional
    override fun modify(id: Long, todo: Todo): Long {
        val entity = todoJpaRepository.findByIdOrNull(id)
            ?: throw CoreException(TodoErrorCode.TODO_NOT_FOUND)

        entity.title = todo.title
        entity.categoryId = todo.categoryId
        entity.scheduledDate = todo.scheduledDate
        entity.notificationTime = todo.notificationTime
        entity.isDone = todo.isDone

        return todoJpaRepository.save(entity).id!!
    }
}
