package com.benecia.lifetracker.auth

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.user.exception.UserErrorCode
import com.benecia.lifetracker.user.service.UserService
import com.benecia.lifetracker.util.JwtUtil
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
    private val tokenBlacklistRepository: TokenBlacklistRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userService: UserService,
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

    fun logout(accessToken: String, refreshToken: String?) {
        // AT Blacklist 처리
        logout(accessToken)

        if (!refreshToken.isNullOrBlank()) {
            try {
                val userId = jwtUtil.extractUserId(refreshToken)
                refreshTokenRepository.delete(UUID.fromString(userId))
            } catch (e: Exception) {}
        }
    }

    fun reissue(refreshToken: String): String {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw CoreException(UserErrorCode.EXPIRED_REFRESH_TOKEN)
        }

        val userIdStr = jwtUtil.extractUserId(refreshToken)
        val userId = UUID.fromString(userIdStr)

        val storedRefreshToken = refreshTokenRepository.get(userId)
        if (storedRefreshToken == null || storedRefreshToken != refreshToken) {
            throw CoreException(UserErrorCode.INVALID_REFRESH_TOKEN)
        }

        val user = userService.findById(userId).toUser()

        return jwtUtil.generateToken(
            userId = user.id!!,
            email = user.email,
            displayName = user.displayName,
            profileImageUrl = user.profileImageUrl,
        )
    }
}
