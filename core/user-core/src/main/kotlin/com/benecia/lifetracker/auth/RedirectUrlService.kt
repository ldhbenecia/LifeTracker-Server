package com.benecia.lifetracker.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class RedirectUrlService(
    @Value("\${oauth.redirect.web}") private val webRedirectBaseUrl: String,
) {

    fun getSuccessRedirectUrl(accessToken: String): String {
        return "$webRedirectBaseUrl?accessToken=${urlEncode(accessToken)}"
    }

    fun getErrorRedirectUrl(errorReason: String): String {
        return "$webRedirectBaseUrl?error=${urlEncode(errorReason)}"
    }

    private fun urlEncode(value: String): String = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
}
