package com.benecia.lifetracker.domain.friend.api

import com.benecia.lifetracker.domain.friend.dto.NewFriendRequest
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.test.api.RestDocsTest
import com.benecia.lifetracker.test.api.RestDocsUtils.requestPreprocessor
import com.benecia.lifetracker.test.api.RestDocsUtils.responsePreprocessor
import com.benecia.lifetracker.user.model.info.FriendInfo
import com.benecia.lifetracker.user.service.FriendService
import io.mockk.every
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
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

class FriendControllerTest : RestDocsTest() {
    private lateinit var friendService: FriendService
    private lateinit var controller: FriendController

    @BeforeEach
    fun setUp() {
        friendService = mockk()
        controller = FriendController(friendService)
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
    fun addFriend() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        val request = NewFriendRequest(receiverEmail = "friend@test.com")
        every { friendService.add(userId, request.toNewFriend()) } returns 10L

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/api/v1/friends")
            .then()
            .status(HttpStatus.CREATED)
            .apply(
                document(
                    "addFriend",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("receiverEmail").type(JsonFieldType.STRING).description("친구로 추가할 유저의 이메일"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("생성된 친구 요청 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun findAllByUserId() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        val friendList = listOf(
            FriendInfo(
                id = 1L,
                friendId = UUID.randomUUID(),
                friendDisplayName = "Lim Dong Hyeok",
                friendProfileImageUrl = "https://img.com/1.png",
            ),
        )
        every { friendService.findAllByUserId(userId) } returns friendList

        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/friends")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "findAllFriends",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("친구 요청 ID"),
                        fieldWithPath("data[].friendId").type(JsonFieldType.STRING).description("친구의 UUID"),
                        fieldWithPath("data[].friendDisplayName").type(JsonFieldType.STRING).description("친구 이름"),
                        fieldWithPath("data[].friendProfileImageUrl").type(JsonFieldType.STRING).description("친구 프로필 이미지 URL").optional(),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun findPendingRequests() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        val pendingList = listOf(
            FriendInfo(
                id = 2L,
                friendId = UUID.randomUUID(),
                friendDisplayName = "요청자1",
                friendProfileImageUrl = "https://img.com/2.png",
            ),
        )
        every { friendService.findPendingRequests(userId) } returns pendingList

        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/friends/requests")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "findPendingFriendRequests",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("친구 요청 ID"),
                        fieldWithPath("data[].friendId").type(JsonFieldType.STRING).description("요청자 UUID"),
                        fieldWithPath("data[].friendDisplayName").type(JsonFieldType.STRING).description("요청자 이름"),
                        fieldWithPath("data[].friendProfileImageUrl").type(JsonFieldType.STRING).description("요청자 프로필 이미지 URL").optional(),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun acceptRequest() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        val friendRequestId = 100L
        every { friendService.acceptRequest(userId, friendRequestId) } returns friendRequestId

        given()
            .contentType(ContentType.JSON)
            .post("/api/v1/friends/{friendRequestId}/accept", friendRequestId)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "acceptFriendRequest",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("friendRequestId").description("친구 요청 ID"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("수락된 친구 요청 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun rejectRequest() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        val friendRequestId = 100L
        every { friendService.rejectRequest(userId, friendRequestId) } returns friendRequestId

        given()
            .contentType(ContentType.JSON)
            .post("/api/v1/friends/{friendRequestId}/reject", friendRequestId)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "rejectFriendRequest",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("friendRequestId").description("친구 요청 ID"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("거절된 친구 요청 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun deleteFriend() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        val friendId = 100L
        every { friendService.delete(userId, friendId) } returns Unit

        given()
            .contentType(ContentType.JSON)
            .delete("/api/v1/friends/{friendId}", friendId)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "deleteFriend",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("friendId").description("삭제할 친구 관계 ID"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터 (없음)"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }
}
