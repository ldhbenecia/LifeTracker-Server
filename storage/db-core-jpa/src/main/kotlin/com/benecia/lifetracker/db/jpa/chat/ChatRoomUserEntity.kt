package com.benecia.lifetracker.db.jpa.chat

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
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
    var visible: Boolean = true,
)

@Embeddable
class ChatRoomUserId(

    @Column(name = "room_id")
    val roomId: Long,

    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    val userId: UUID,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ChatRoomUserId

        if (roomId != other.roomId) return false
        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = roomId.hashCode()
        result = 31 * result + userId.hashCode()
        return result
    }
}
