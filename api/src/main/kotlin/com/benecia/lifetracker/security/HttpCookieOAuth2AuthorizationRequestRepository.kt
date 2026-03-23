package com.benecia.lifetracker.security

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import org.springframework.util.SerializationUtils
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.util.*

@Component
class HttpCookieOAuth2AuthorizationRequestRepository : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    companion object {
        private const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        private const val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"
        private const val COOKIE_EXPIRE_SECONDS = 180
    }

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            ?.let { deserialize(it) }
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response)
            return
        }

        addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS)
        val redirectUriAfterLogin = request.getParameter("redirect_uri")
        if (redirectUriAfterLogin.isNotBlank()) {
            addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS)
        }
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): OAuth2AuthorizationRequest? {
        return loadAuthorizationRequest(request)
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest, response: HttpServletResponse) {
        deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
    }

    private fun getCookie(request: HttpServletRequest, name: String): String? {
        return request.cookies?.find { it.name == name }?.value
    }

    private fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.maxAge = maxAge
        response.addCookie(cookie)
    }

    private fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
        request.cookies?.find { it.name == name }?.let {
            val cookie = Cookie(name, "")
            cookie.path = "/"
            cookie.isHttpOnly = true
            cookie.maxAge = 0
            response.addCookie(cookie)
        }
    }

    private fun serialize(obj: OAuth2AuthorizationRequest): String {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj))
    }

    private fun deserialize(cookie: String): OAuth2AuthorizationRequest {
        val bytes = Base64.getUrlDecoder().decode(cookie)
        ObjectInputStream(ByteArrayInputStream(bytes)).use { ois ->
            return ois.readObject() as OAuth2AuthorizationRequest
        }
    }
}
