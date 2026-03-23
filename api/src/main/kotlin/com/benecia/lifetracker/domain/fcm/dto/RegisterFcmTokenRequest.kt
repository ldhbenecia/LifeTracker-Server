package com.benecia.lifetracker.domain.fcm.dto

import com.benecia.lifetracker.user.service.DeviceType

data class RegisterFcmTokenRequest(
    val token: String,
    val deviceType: DeviceType,
)
