package com.benecia.lifetracker.db.jpa.chat

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "chat_room_user")
class ChatRoomUserEntity(

    @EmbeddedId
    val id: ChatRoomUserId,

    val joinedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "notification_enabled")
    var notificationEnabled: Boolean = true,

    @Column(name = "visible")
    var visible: Boolean = true
)

@Embeddable
class ChatRoomUserId(

    @Column(name = "room_id")
    val roomId: Long,

    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    val userId: UUID
) : Serializable