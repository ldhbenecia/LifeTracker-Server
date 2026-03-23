package com.benecia.lifetracker.notification.todo

import com.benecia.lifetracker.notification.FcmNotificationService
import com.benecia.lifetracker.todocore.service.TodoRepository
import com.benecia.lifetracker.user.service.FcmTokenService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TodoNotificationScheduler(
    private val todoRepository: TodoRepository,
    private val fcmTokenService: FcmTokenService,
    private val fcmNotificationService: FcmNotificationService,
) {
    @Scheduled(fixedDelay = 60_000)
    fun sendTodoNotifications() {
        val now = LocalDateTime.now()
        val todos = todoRepository.findAllByNotificationTimeBetween(now, now.plusMinutes(1))
        todos.forEach { todo ->
            fcmTokenService.findAllByUserId(todo.userId).forEach { fcmToken ->
                fcmNotificationService.send(
                    token = fcmToken.token,
                    title = "할 일 알림",
                    body = todo.title,
                    data = mapOf("type" to "todo_reminder", "id" to todo.id.toString()),
                )
            }
        }
    }
}
