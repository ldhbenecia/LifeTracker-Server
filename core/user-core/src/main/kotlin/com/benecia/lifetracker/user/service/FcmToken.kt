package com.benecia.lifetracker.user.service

import java.util.UUID

data class FcmToken(
    val id: Long? = null,
    val userId: UUID,
    val token: String,
    val deviceType: DeviceType,
)
