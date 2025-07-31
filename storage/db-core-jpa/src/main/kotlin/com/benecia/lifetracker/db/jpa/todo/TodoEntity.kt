package com.benecia.lifetracker.db.jpa.todo

import com.benecia.lifetracker.db.jpa.BaseEntity
import com.benecia.lifetracker.todocore.service.Todo
import com.benecia.lifetracker.todocore.service.TodoStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Entity
@SQLRestriction("status = 'ACTIVE'")
@Table(name = "todo")
class TodoEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    var categoryId: Long,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var scheduledDate: LocalDate,

    var scheduledTime: LocalTime? = null,

    var notificationTime: LocalDateTime? = null,

    @Column(nullable = false)
    var isDone: Boolean,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: TodoStatus = TodoStatus.ACTIVE,

) : BaseEntity() {

    companion object {
        fun from(todo: Todo): TodoEntity {
            return TodoEntity(
                id = todo.id,
                userId = todo.userId,
                title = todo.title,
                categoryId = todo.categoryId,
                scheduledDate = todo.scheduledDate,
                scheduledTime = todo.scheduledTime,
                notificationTime = todo.notificationTime,
                isDone = todo.isDone,
                status = todo.status,
            )
        }
    }

    fun toDomain(): Todo = Todo(
        id = this.id,
        userId = this.userId,
        title = this.title,
        categoryId = this.categoryId,
        scheduledDate = this.scheduledDate,
        scheduledTime = this.scheduledTime,
        notificationTime = this.notificationTime,
        isDone = this.isDone,
        status = this.status,
    )

    fun remove() {
        this.status = TodoStatus.DELETED
    }
}
