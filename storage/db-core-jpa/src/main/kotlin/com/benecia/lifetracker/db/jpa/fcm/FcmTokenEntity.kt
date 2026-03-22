package com.benecia.lifetracker.db.jpa.fcm

import com.benecia.lifetracker.db.jpa.BaseEntity
import com.benecia.lifetracker.user.service.DeviceType
import com.benecia.lifetracker.user.service.FcmToken
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.UUID

@Entity
@Table(
    name = "fcm_token",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "token"])],
    indexes = [Index(name = "idx_fcm_token_user_id", columnList = "user_id")],
)
class FcmTokenEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    val userId: UUID,

    @Column(nullable = false)
    val token: String,

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    val deviceType: DeviceType,

) : BaseEntity() {

    companion object {
        fun from(fcmToken: FcmToken): FcmTokenEntity = FcmTokenEntity(
            userId = fcmToken.userId,
            token = fcmToken.token,
            deviceType = fcmToken.deviceType,
        )
    }

    fun toDomain(): FcmToken = FcmToken(
        id = this.id,
        userId = this.userId,
        token = this.token,
        deviceType = this.deviceType,
    )
}
