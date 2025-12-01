package com.benecia.lifetracker.auth

import org.springframework.stereotype.Repository

@Repository
interface TokenBlacklistRepository {
    fun blacklistToken(token: String, ttlInMillis: Long)
    fun isBlacklisted(token: String): Boolean
}
