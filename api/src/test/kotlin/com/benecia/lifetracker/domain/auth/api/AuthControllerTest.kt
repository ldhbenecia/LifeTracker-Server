package com.benecia.lifetracker.domain.auth.api

import com.benecia.lifetracker.auth.AuthService
import com.benecia.lifetracker.domain.auth.AuthController
import com.benecia.lifetracker.test.api.RestDocsTest
import com.benecia.lifetracker.test.api.RestDocsUtils.requestPreprocessor
import com.benecia.lifetracker.test.api.RestDocsUtils.responsePreprocessor
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.restassured.http.ContentType
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName
import org.springframework.restdocs.cookies.CookieDocumentation.requestCookies
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields

class AuthControllerTest : RestDocsTest() {
    private lateinit var authService: AuthService
    private lateinit var controller: AuthController

    @BeforeEach
    fun setUp() {
        authService = mockk()
        controller = AuthController(authService)
        mockMvc = mockController(controller)
    }

    @Test
    fun logout() {
        val accessToken = "access.token.payload"
        val refreshToken = "refresh.token.payload"

        // Mocking: 로그아웃은 반환값이 없으므로 just runs
        every { authService.logout(any(), any()) } just runs

        val servletCookie = Cookie("refresh_token", refreshToken)

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer $accessToken")
            .interceptor { requestBuilder ->
                requestBuilder.cookie(servletCookie)
                requestBuilder
            }
            .post("/api/auth/logout")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "auth-logout",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer Access Token"),
                    ),
                    requestCookies(
                        cookieWithName("refresh_token").description("Refresh Token (HttpOnly Cookie)"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.STRING).description("결과 메시지"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun reissue() {
        val refreshToken = "valid.refresh.token"
        val newAccessToken = "new.access.token.payload"

        // Mocking: 재발급 성공 시 새로운 Access Token 반환
        every { authService.reissue(any()) } returns newAccessToken

        val servletCookie = Cookie("refresh_token", refreshToken)

        given()
            .contentType(ContentType.JSON)
            .interceptor { requestBuilder ->
                requestBuilder.cookie(servletCookie)
                requestBuilder
            }
            .post("/api/auth/reissue")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "auth-reissue",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestCookies(
                        cookieWithName("refresh_token").description("Refresh Token (HttpOnly Cookie)"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.STRING).description("새로 발급된 Access Token"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }
}
