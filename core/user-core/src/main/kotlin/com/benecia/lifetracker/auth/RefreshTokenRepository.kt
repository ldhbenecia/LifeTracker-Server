package com.benecia.lifetracker.auth

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RefreshTokenRepository {
    fun save(userId: UUID, refreshToken: String, ttlInMillis: Long)
    fun get(userId: UUID): String?
    fun delete(userId: UUID)
}
