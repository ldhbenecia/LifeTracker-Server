package com.benecia.lifetracker.db.jpa.todo

import com.benecia.lifetracker.todocore.service.Todo
import com.benecia.lifetracker.todocore.service.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Repository
class TodoEntityRepository(
    private val todoJpaRepository: TodoJpaRepository,
) : TodoRepository {

    override fun findByUserIdAndId(userId: UUID, id: Long): Todo? {
        return todoJpaRepository.findByUserIdAndId(userId, id)?.toDomain()
    }

    override fun findByUserIdAndScheduledDateRange(
        userId: UUID,
        start: LocalDate,
        end: LocalDate,
    ): List<Todo> {
        val entities = todoJpaRepository.findByUserIdAndScheduledDateBetween(userId, start, end)
        return entities.map { it.toDomain() }
    }

    override fun add(todo: Todo): Long {
        val entity = TodoEntity.from(todo)
        return todoJpaRepository.save(entity).id!!
    }

    @Transactional
    override fun modify(id: Long, todo: Todo): Long? {
        val entity = todoJpaRepository.findByIdOrNull(id) ?: return null

        entity.title = todo.title
        entity.categoryId = todo.categoryId
        entity.scheduledDate = todo.scheduledDate
        entity.scheduledTime = todo.scheduledTime
        entity.notificationTime = todo.notificationTime
        entity.isDone = todo.isDone
        entity.status = todo.status

        return todoJpaRepository.save(entity).id
    }

    override fun remove(id: Long): Long? {
        val entity = todoJpaRepository.findByIdOrNull(id) ?: return null
        entity.remove()
        return todoJpaRepository.save(entity).id
    }
}
