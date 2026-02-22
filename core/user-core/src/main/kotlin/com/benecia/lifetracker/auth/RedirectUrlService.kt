package com.benecia.lifetracker.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class RedirectUrlService(
    @Value("\${oauth.redirect.web}") private val webRedirectBaseUrl: String,
    @Value("\${oauth.redirect.app}") private val appRedirectBaseUrl: String,
) {

    fun getSuccessRedirectUrl(accessToken: String, refreshToken: String): String {
        return "$appRedirectBaseUrl?accessToken=${urlEncode(accessToken)}&refreshToken=${urlEncode(refreshToken)}"
    }

    fun getErrorRedirectUrl(errorReason: String): String {
        return "$appRedirectBaseUrl?error=${urlEncode(errorReason)}"
    }

    private fun urlEncode(value: String): String = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
}
