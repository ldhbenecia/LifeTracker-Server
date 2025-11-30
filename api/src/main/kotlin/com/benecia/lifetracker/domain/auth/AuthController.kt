package com.benecia.lifetracker.domain.auth

import com.benecia.lifetracker.auth.AuthService
import com.benecia.lifetracker.common.response.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ApiResponse<String> {
        val authHeader = request.getHeader("Authorization")
        if (!authHeader.isNullOrBlank() && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            authService.logout(token)
        }

        return ApiResponse.success("성공적으로 로그아웃 되었습니다.")
    }
}
