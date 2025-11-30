package com.benecia.lifetracker.auth

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class TokenBlacklistRepository(
    private val redisTemplate: StringRedisTemplate,
) {

    fun blacklistToken(token: String, remainingMilliSeconds: Long) {
        redisTemplate.opsForValue().set(
            "blacklist:$token",
            "logout",
            remainingMilliSeconds,
            TimeUnit.MILLISECONDS,
        )
    }

    fun isBlacklisted(token: String): Boolean {
        return redisTemplate.hasKey("blacklist:$token")
    }
}
