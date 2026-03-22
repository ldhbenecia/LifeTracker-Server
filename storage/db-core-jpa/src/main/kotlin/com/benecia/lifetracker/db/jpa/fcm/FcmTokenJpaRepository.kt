package com.benecia.lifetracker.db.jpa.fcm

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FcmTokenJpaRepository : JpaRepository<FcmTokenEntity, Long> {
    fun findByUserIdAndToken(userId: UUID, token: String): FcmTokenEntity?
    fun findAllByUserId(userId: UUID): List<FcmTokenEntity>
    fun deleteByUserIdAndToken(userId: UUID, token: String)
}
