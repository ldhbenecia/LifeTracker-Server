package com.benecia.lifetracker.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FcmNotificationService {

    private val logger = LoggerFactory.getLogger(FcmNotificationService::class.java)

    fun send(token: String, title: String, body: String, data: Map<String, String>) {
        val message = Message.builder()
            .setToken(token)
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build(),
            )
            .putAllData(data)
            .build()
        try {
            FirebaseMessaging.getInstance().send(message)
        } catch (e: FirebaseMessagingException) {
            logger.error("FCM 발송 실패. token={}, error={}", token, e.message)
        }
    }
}
