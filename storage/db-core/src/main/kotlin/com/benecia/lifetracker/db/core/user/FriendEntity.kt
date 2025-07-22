package com.benecia.lifetracker.db.core.user

import com.benecia.lifetracker.user.service.Friend
import com.benecia.lifetracker.user.service.FriendStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.CreationTimestamp
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(
    name = "friend",
    uniqueConstraints = [UniqueConstraint(columnNames = ["requester_id", "receiver_id"])],
)
class FriendEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val requesterId: UUID,

    @Column(nullable = false)
    val receiverId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: FriendStatus = FriendStatus.PENDING,

    @CreationTimestamp
    val requestAt: ZonedDateTime? = null,
) {
    companion object {
        fun from(friend: Friend): FriendEntity {
            return FriendEntity(
                requesterId = friend.requesterId,
                receiverId = friend.receiverId,
                status = friend.status,
            )
        }
    }

    fun toDomain(): Friend = Friend(
        id = this.id,
        requesterId = this.requesterId,
        receiverId = this.receiverId,
        status = this.status,
    )
}
