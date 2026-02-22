package com.benecia.lifetracker.security.handler

import com.benecia.lifetracker.auth.RedirectUrlService
import com.benecia.lifetracker.auth.RefreshTokenRepository
import com.benecia.lifetracker.security.HttpCookieOAuth2AuthorizationRequestRepository
import com.benecia.lifetracker.user.service.User
import com.benecia.lifetracker.user.service.UserService
import com.benecia.lifetracker.util.JwtUtil
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2AuthenticationSuccessHandler(
    private val jwtUtil: JwtUtil,
    private val userService: UserService,
    private val redirectUrlService: RedirectUrlService,
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val oAuth2User = authentication.principal as OAuth2User
        val registrationId = getRegistrationId(request)

        try {
            val user = createOrUpdateUser(oAuth2User, registrationId)
            val accessToken = jwtUtil.generateToken(user.id!!, user.email, user.displayName, user.profileImageUrl)
            val refreshToken = jwtUtil.generateRefreshToken(user.id)
            refreshTokenRepository.save(
                user.id,
                refreshToken,
                jwtUtil.getRefreshExpiration(),
            )
            addRefreshTokenCookie(response, refreshToken)

            val redirectUrl = redirectUrlService.getSuccessRedirectUrl(accessToken, refreshToken)
            clearAuthenticationAttributes(request, response)
            redirectStrategy.sendRedirect(request, response, redirectUrl)
        } catch (e: Exception) {
            logger.error("OAuth2 인증 성공 처리 중 오류 발생", e)
            val errorUrl = redirectUrlService.getErrorRedirectUrl("authentication_failed")
            redirectStrategy.sendRedirect(request, response, errorUrl)
        }
    }

    private fun addRefreshTokenCookie(response: HttpServletResponse, token: String) {
        val cookie = Cookie("refresh_token", token)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.maxAge = (jwtUtil.getRefreshExpiration() / 1000).toInt()
        response.addCookie(cookie)
    }

    private fun createOrUpdateUser(oAuth2User: OAuth2User, registrationId: String): User {
        val userInfo = extractUserInfo(oAuth2User, registrationId)
        val existingUser = userService.findByProviderAndEmail(registrationId, userInfo.email)

        return if (existingUser != null) {
            val updatedUser = existingUser.copy(
                displayName = userInfo.name,
                profileImageUrl = userInfo.profileImage,
            )
            userService.update(updatedUser)
        } else {
            val newUser = User(
                provider = registrationId,
                email = userInfo.email,
                displayName = userInfo.name,
                profileImageUrl = userInfo.profileImage,
            )
            userService.add(newUser)
        }
    }

    private fun extractUserInfo(oAuth2User: OAuth2User, registrationId: String): UserInfo {
        return when (registrationId) {
            "google" -> UserInfo(
                email = oAuth2User.getAttribute<String>("email") ?: "",
                name = oAuth2User.getAttribute<String>("name") ?: "Unknown",
                profileImage = oAuth2User.getAttribute<String>("picture"),
            )
            "naver" -> {
                val response = oAuth2User.getAttribute<Map<String, Any>>("response")
                UserInfo(
                    email = response?.get("email") as? String ?: "",
                    name = response?.get("name") as? String ?: "Unknown",
                    profileImage = response?.get("profile_image") as? String,
                )
            }
            "kakao" -> {
                val kakaoAccount = oAuth2User.getAttribute<Map<String, Any>>("kakao_account")
                val profile = kakaoAccount?.get("profile") as? Map<*, *>
                UserInfo(
                    email = kakaoAccount?.get("email") as? String ?: "",
                    name = profile?.get("nickname") as? String ?: "Unknown",
                    profileImage = profile?.get("profile_image_url") as? String,
                )
            }
            else -> throw IllegalArgumentException("Unsupported provider: $registrationId")
        }
    }

    private fun getRegistrationId(request: HttpServletRequest): String {
        return request.requestURI.split("/").last()
    }

    private fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    data class UserInfo(
        val email: String,
        val name: String,
        val profileImage: String?,
    )
}
