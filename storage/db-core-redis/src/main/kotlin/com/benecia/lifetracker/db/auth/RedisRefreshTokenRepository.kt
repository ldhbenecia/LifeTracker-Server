package com.benecia.lifetracker.db.auth

import com.benecia.lifetracker.auth.RefreshTokenRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.TimeUnit

@Repository
class RedisRefreshTokenRepository(
    private val redisTemplate: StringRedisTemplate,
) : RefreshTokenRepository {

    override fun save(userId: UUID, refreshToken: String, ttlInMillis: Long) {
        redisTemplate.opsForValue().set(
            "RT:$userId",
            refreshToken,
            ttlInMillis,
            TimeUnit.MILLISECONDS,
        )
    }

    override fun get(userId: UUID): String? {
        return redisTemplate.opsForValue().get("RT:$userId")
    }

    override fun delete(userId: UUID) {
        redisTemplate.delete("RT:$userId")
    }
}
