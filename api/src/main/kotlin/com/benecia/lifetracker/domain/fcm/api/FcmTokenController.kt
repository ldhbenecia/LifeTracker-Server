package com.benecia.lifetracker.domain.fcm.api

import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.domain.fcm.dto.RegisterFcmTokenRequest
import com.benecia.lifetracker.domain.fcm.dto.UnregisterFcmTokenRequest
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.user.service.FcmTokenService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/fcm/token")
class FcmTokenController(
    private val fcmTokenService: FcmTokenService,
) {
    @PutMapping
    fun register(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestBody request: RegisterFcmTokenRequest,
    ): ApiResponse<Long> {
        val id = fcmTokenService.register(loginUser.id, request.token, request.deviceType)
        return ApiResponse.success(id)
    }

    @DeleteMapping
    fun unregister(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestBody request: UnregisterFcmTokenRequest,
    ): ApiResponse<Unit> {
        fcmTokenService.unregister(loginUser.id, request.token)
        return ApiResponse.success()
    }
}
