package com.benecia.lifetracker.auth

import com.benecia.lifetracker.util.JwtUtil
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val tokenBlacklistRepository: TokenBlacklistRepository,
    private val jwtUtil: JwtUtil,
) {

    fun logout(accessToken: String) {
        if (jwtUtil.isTokenExpired(accessToken)) {
            return
        }

        val expiration = jwtUtil.extractExpiration(accessToken)
        val now = System.currentTimeMillis()
        val ttl = expiration.time - now

        if (ttl > 0) {
            tokenBlacklistRepository.blacklistToken(accessToken, ttl)
        }
    }
}
