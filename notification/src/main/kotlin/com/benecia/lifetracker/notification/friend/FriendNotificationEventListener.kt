package com.benecia.lifetracker.notification.friend

import com.benecia.lifetracker.notification.FcmNotificationService
import com.benecia.lifetracker.user.event.FriendRequestAcceptedEvent
import com.benecia.lifetracker.user.event.FriendRequestSentEvent
import com.benecia.lifetracker.user.service.FcmTokenService
import com.benecia.lifetracker.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class FriendNotificationEventListener(
    private val fcmNotificationService: FcmNotificationService,
    private val fcmTokenService: FcmTokenService,
    private val userService: UserService,
) {

    private val logger = LoggerFactory.getLogger(FriendNotificationEventListener::class.java)

    @Async
    @TransactionalEventListener
    fun handleFriendRequestSent(event: FriendRequestSentEvent) {
        try {
            val requester = userService.findById(event.requesterId)
            fcmTokenService.findAllByUserId(event.receiverId).forEach { fcmToken ->
                fcmNotificationService.send(
                    token = fcmToken.token,
                    title = "새 친구 요청",
                    body = "${requester.displayName}님이 친구 요청을 보냈습니다.",
                    data = mapOf("type" to "friend_request", "id" to event.friendRequestId.toString()),
                )
            }
        } catch (e: Exception) {
            logger.error("친구 요청 알림 발송 실패. requesterId={}, receiverId={}", event.requesterId, event.receiverId, e)
        }
    }

    @Async
    @TransactionalEventListener
    fun handleFriendRequestAccepted(event: FriendRequestAcceptedEvent) {
        try {
            val acceptor = userService.findById(event.receiverId)
            fcmTokenService.findAllByUserId(event.requesterId).forEach { fcmToken ->
                fcmNotificationService.send(
                    token = fcmToken.token,
                    title = "친구 요청 수락",
                    body = "${acceptor.displayName}님이 친구 요청을 수락했습니다.",
                    data = mapOf("type" to "friend_accepted", "id" to event.friendRequestId.toString()),
                )
            }
        } catch (e: Exception) {
            logger.error("친구 수락 알림 발송 실패. requesterId={}, receiverId={}", event.requesterId, event.receiverId, e)
        }
    }
}
