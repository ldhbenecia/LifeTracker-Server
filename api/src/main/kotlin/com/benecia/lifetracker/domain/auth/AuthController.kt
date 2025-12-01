package com.benecia.lifetracker.domain.auth

import com.benecia.lifetracker.auth.AuthService
import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.user.exception.UserErrorCode
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @CookieValue(name = "refresh_token", required = false) refreshToken: String?,
    ): ApiResponse<String> {
        val authHeader = request.getHeader("Authorization")
        if (!authHeader.isNullOrBlank() && authHeader.startsWith("Bearer ")) {
            val accessToken = authHeader.substring(7)
            authService.logout(accessToken, refreshToken)
        }

        // 클라이언트 쿠키 삭제 처리 (MaxAge = 0)
        val cookie = Cookie("refresh_token", null)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.maxAge = 0
        response.addCookie(cookie)

        return ApiResponse.success("성공적으로 로그아웃 되었습니다.")
    }

    @PostMapping("/reissue")
    fun reissue(
        @CookieValue(name = "refresh_token", required = false) refreshToken: String?,
    ): ApiResponse<String> {
        if (refreshToken.isNullOrBlank()) {
            throw CoreException(UserErrorCode.REFRESH_TOKEN_NOT_FOUND)
        }

        val newAccessToken = authService.reissue(refreshToken)
        return ApiResponse.success(newAccessToken)
    }
}
