package com.benecia.lifetracker.notification.friend

import com.benecia.lifetracker.notification.FcmNotificationService
import com.benecia.lifetracker.user.event.FriendRequestAcceptedEvent
import com.benecia.lifetracker.user.event.FriendRequestSentEvent
import com.benecia.lifetracker.user.service.FcmTokenService
import com.benecia.lifetracker.user.service.UserService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class FriendNotificationEventListener(
    private val fcmNotificationService: FcmNotificationService,
    private val fcmTokenService: FcmTokenService,
    private val userService: UserService,
) {
    @EventListener
    fun handleFriendRequestSent(event: FriendRequestSentEvent) {
        val requester = userService.findById(event.requesterId)
        fcmTokenService.findAllByUserId(event.receiverId).forEach { fcmToken ->
            fcmNotificationService.send(
                token = fcmToken.token,
                title = "새 친구 요청",
                body = "${requester.displayName}님이 친구 요청을 보냈습니다.",
                data = mapOf("type" to "friend_request", "id" to event.friendRequestId.toString()),
            )
        }
    }

    @EventListener
    fun handleFriendRequestAccepted(event: FriendRequestAcceptedEvent) {
        val acceptor = userService.findById(event.receiverId)
        fcmTokenService.findAllByUserId(event.requesterId).forEach { fcmToken ->
            fcmNotificationService.send(
                token = fcmToken.token,
                title = "친구 요청 수락",
                body = "${acceptor.displayName}님이 친구 요청을 수락했습니다.",
                data = mapOf("type" to "friend_accepted", "id" to event.friendRequestId.toString()),
            )
        }
    }
}
