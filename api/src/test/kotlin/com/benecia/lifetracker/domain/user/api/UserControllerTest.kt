package com.benecia.lifetracker.domain.user.api

import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.test.api.RestDocsTest
import com.benecia.lifetracker.test.api.RestDocsUtils.requestPreprocessor
import com.benecia.lifetracker.test.api.RestDocsUtils.responsePreprocessor
import com.benecia.lifetracker.user.model.info.UserInfo
import com.benecia.lifetracker.user.service.User
import com.benecia.lifetracker.user.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

class UserControllerTest : RestDocsTest() {
    private lateinit var userService: UserService
    private lateinit var controller: UserController

    @BeforeEach
    fun setUp() {
        userService = mockk()
        controller = UserController(userService)
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
    fun findUserById() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)
        setupAuthentication(loginUser)

        val userInfo = UserInfo(
            id = userId,
            provider = "GOOGLE",
            email = "test@test.com",
            displayName = "테스트유저",
            profileImageUrl = "https://profile.com/img.png",
            userCode = "ABCD1234",
        )
        every { userService.findById(userId) } returns userInfo

        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/users/{id}", userId)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "findUserById",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("유저 UUID"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.STRING).description("유저 UUID"),
                        fieldWithPath("data.provider").type(JsonFieldType.STRING).description("OAuth 프로바이더"),
                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("표시 이름"),
                        fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL").optional(),
                        fieldWithPath("data.userCode").type(JsonFieldType.STRING).description("유저 코드 (8자리 영숫자)"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun searchByUserCode() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)
        setupAuthentication(loginUser)

        val user = User(
            id = UUID.randomUUID(),
            provider = "GOOGLE",
            email = "friend@test.com",
            displayName = "친구유저",
            profileImageUrl = "https://profile.com/friend.png",
            userCode = "XYZW5678",
        )
        every { userService.findByUserCode("XYZW5678") } returns user

        given()
            .contentType(ContentType.JSON)
            .queryParam("code", "XYZW5678")
            .get("/api/v1/users/search")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "searchUserByCode",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    queryParameters(
                        parameterWithName("code").description("검색할 유저 코드 (8자리 영숫자)"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.userCode").type(JsonFieldType.STRING).description("유저 코드"),
                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("표시 이름"),
                        fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL").optional(),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }
}
