package com.benecia.lifetracker.user.service

import java.util.UUID

interface FcmTokenRepository {
    fun save(fcmToken: FcmToken): Long
    fun delete(userId: UUID, token: String)
    fun findAllByUserId(userId: UUID): List<FcmToken>
}
