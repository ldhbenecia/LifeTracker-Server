package com.benecia.lifetracker.db.auth

import com.benecia.lifetracker.auth.TokenBlacklistRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RedisTokenBlacklistRepository(
    private val redisTemplate: StringRedisTemplate,
) : TokenBlacklistRepository {

    override fun blacklistToken(token: String, ttlInMillis: Long) {
        redisTemplate.opsForValue().set(
            "blacklist:$token",
            "logout",
            ttlInMillis,
            TimeUnit.MILLISECONDS,
        )
    }

    override fun isBlacklisted(token: String): Boolean {
        return redisTemplate.hasKey("blacklist:$token")
    }
}
