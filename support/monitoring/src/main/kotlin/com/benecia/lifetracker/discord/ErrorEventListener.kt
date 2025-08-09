package com.benecia.lifetracker.discord

import com.benecia.lifetracker.common.event.ErrorOccuredEvent
import com.benecia.lifetracker.common.exception.CoreException
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class ErrorEventListener(
    private val discordWebhookService: DiscordWebhookService,
) {

    @Async
    @EventListener
    fun onError(event: ErrorOccuredEvent) {
        val ex = event.exception

        val (status, name, message, type) = if (ex is CoreException) {
            listOf(
                ex.errorCode.code.toString(),
                ex.errorCode.name,
                ex.errorCode.message,
                ex.javaClass.simpleName,
            )
        } else {
            listOf(
                "500",
                ex.javaClass.simpleName,
                ex.message ?: "No message",
                ex.javaClass.name,
            )
        }

        val stackTraceFirst = ex.stackTrace.firstOrNull()?.toString() ?: "No stacktrace"
        val stackTraceDetail = ex.stackTrace
            .take(5)
            .joinToString("\n") { it.toString() }

        discordWebhookService.sendErrorNotification(
            title = "🚨 [$type] 예외 발생",
            description = "에러 종류: $name | 상태: $status",
            color = 0xFF0000,
            fields = listOf(
                "에러 이름" to name,
                "상태 코드" to status,
                "설명" to message,
                "발생 위치" to stackTraceFirst,
                "상세 정보" to "```\n$stackTraceDetail\n```",
            ),
        )
    }
}
