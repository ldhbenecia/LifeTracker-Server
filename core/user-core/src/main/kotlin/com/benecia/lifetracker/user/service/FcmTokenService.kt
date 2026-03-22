package com.benecia.lifetracker.user.service

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FcmTokenService(
    private val fcmTokenRepository: FcmTokenRepository,
) {
    fun register(userId: UUID, token: String, deviceType: DeviceType): Long {
        return fcmTokenRepository.save(FcmToken(userId = userId, token = token, deviceType = deviceType))
    }

    fun unregister(userId: UUID, token: String) {
        fcmTokenRepository.delete(userId, token)
    }

    fun findAllByUserId(userId: UUID): List<FcmToken> {
        return fcmTokenRepository.findAllByUserId(userId)
    }
}
