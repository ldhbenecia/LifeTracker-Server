package com.benecia.lifetracker.security.handler

import com.benecia.lifetracker.auth.RedirectUrlService
import com.benecia.lifetracker.security.HttpCookieOAuth2AuthorizationRequestRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class OAuth2AuthenticationFailureHandler(
    private val redirectUrlService: RedirectUrlService,
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
) : SimpleUrlAuthenticationFailureHandler() {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException,
    ) {
        val errorUrl = redirectUrlService.getErrorRedirectUrl(exception.message ?: "authentication_failed")

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
        redirectStrategy.sendRedirect(request, response, errorUrl)
    }
}
