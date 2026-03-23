package com.benecia.lifetracker.domain.fcm.api

import com.benecia.lifetracker.domain.fcm.dto.RegisterFcmTokenRequest
import com.benecia.lifetracker.domain.fcm.dto.UnregisterFcmTokenRequest
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.test.api.RestDocsTest
import com.benecia.lifetracker.test.api.RestDocsUtils.requestPreprocessor
import com.benecia.lifetracker.test.api.RestDocsUtils.responsePreprocessor
import com.benecia.lifetracker.user.service.DeviceType
import com.benecia.lifetracker.user.service.FcmTokenService
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

class FcmTokenControllerTest : RestDocsTest() {
    private lateinit var fcmTokenService: FcmTokenService
    private lateinit var controller: FcmTokenController

    @BeforeEach
    fun setUp() {
        fcmTokenService = mockk()
        controller = FcmTokenController(fcmTokenService)
        mockMvc = mockController(controller)
    }

    private fun setupAuthentication(loginUser: LoginUser) {
        val auth = UsernamePasswordAuthenticationToken(loginUser, null, loginUser.authorities)
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun createLoginUser(userId: UUID): LoginUser {
        return LoginUser(
            id = userId,
            email = "test@test.com",
            displayName = "테스트유저",
            profileImageUrl = "https://profile.com/img.png",
        )
    }

    @Test
    fun registerFcmToken() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)
        setupAuthentication(loginUser)

        val request = RegisterFcmTokenRequest(
            token = "fcm-device-token-example",
            deviceType = DeviceType.ANDROID,
        )
        every { fcmTokenService.register(userId, request.token, request.deviceType) } returns 1L

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .put("/api/v1/fcm/token")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "registerFcmToken",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("token").type(JsonFieldType.STRING).description("FCM 디바이스 토큰"),
                        fieldWithPath("deviceType").type(JsonFieldType.STRING).description("디바이스 타입 (ANDROID, IOS)"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("등록된 FCM 토큰 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun unregisterFcmToken() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)
        setupAuthentication(loginUser)

        val request = UnregisterFcmTokenRequest(token = "fcm-device-token-example")
        justRun { fcmTokenService.unregister(userId, request.token) }

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .delete("/api/v1/fcm/token")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "unregisterFcmToken",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("token").type(JsonFieldType.STRING).description("삭제할 FCM 디바이스 토큰"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("null"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }
}
