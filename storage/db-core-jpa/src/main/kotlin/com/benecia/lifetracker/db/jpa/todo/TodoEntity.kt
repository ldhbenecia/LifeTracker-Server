package com.benecia.lifetracker.db.jpa.todo

import com.benecia.lifetracker.db.jpa.BaseEntity
import com.benecia.lifetracker.todocore.service.Todo
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
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
    var scheduledDate: LocalDateTime,

    var notificationTime: LocalDateTime?,

    @Column(nullable = false)
    var isDone: Boolean,

) : BaseEntity() {

    companion object {
        fun from(todo: Todo): TodoEntity {
            return TodoEntity(
                id = todo.id,
                userId = todo.userId,
                title = todo.title,
                categoryId = todo.categoryId,
                scheduledDate = todo.scheduledDate,
                notificationTime = todo.notificationTime,
                isDone = todo.isDone,
            )
        }
    }

    fun toDomain(): Todo = Todo(
        id = this.id,
        userId = this.userId,
        title = this.title,
        categoryId = this.categoryId,
        scheduledDate = this.scheduledDate,
        notificationTime = this.notificationTime,
        isDone = this.isDone,
    )
}
