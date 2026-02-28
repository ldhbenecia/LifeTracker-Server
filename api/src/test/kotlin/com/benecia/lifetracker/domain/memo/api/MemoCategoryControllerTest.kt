package com.benecia.lifetracker.domain.memo.api

import com.benecia.lifetracker.domain.memo.dto.AddMemoCategoryRequest
import com.benecia.lifetracker.domain.memo.dto.ModifyMemoCategoryRequest
import com.benecia.lifetracker.memocore.model.info.MemoCategoryInfo
import com.benecia.lifetracker.memocore.service.MemoCategoryService
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.test.api.RestDocsTest
import com.benecia.lifetracker.test.api.RestDocsUtils.requestPreprocessor
import com.benecia.lifetracker.test.api.RestDocsUtils.responsePreprocessor
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
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

@Tag("restdocs")
class MemoCategoryControllerTest : RestDocsTest() {

    private lateinit var memoCategoryService: MemoCategoryService
    private lateinit var controller: MemoCategoryController

    @BeforeEach
    fun setUp() {
        memoCategoryService = mockk()
        controller = MemoCategoryController(memoCategoryService)
        mockMvc = mockController(controller)
    }

    private fun setupAuth(userId: UUID) {
        val loginUser = LoginUser(
            id = userId,
            email = "test@test.com",
            displayName = "테스트유저",
            profileImageUrl = "https://profile.com/img.png",
        )
        val auth = UsernamePasswordAuthenticationToken(loginUser, null, loginUser.authorities)
        SecurityContextHolder.getContext().authentication = auth
    }

    @Test
    fun readCategories() {
        val userId = UUID.randomUUID()
        setupAuth(userId)

        every { memoCategoryService.findAll(userId) } returns listOf(
            MemoCategoryInfo(id = 1L, name = "일상", icon = "icon-daily", color = "#FF6B6B"),
            MemoCategoryInfo(id = 2L, name = "업무", icon = "icon-work", color = "#4ECDC4"),
        )

        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/memos/categories")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "readMemoCategories",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("data[].icon").type(JsonFieldType.STRING).description("카테고리 아이콘"),
                        fieldWithPath("data[].color").type(JsonFieldType.STRING).description("카테고리 색상"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun addCategory() {
        val userId = UUID.randomUUID()
        setupAuth(userId)

        every { memoCategoryService.add(userId, any()) } returns 1L

        given()
            .contentType(ContentType.JSON)
            .body(AddMemoCategoryRequest(name = "일상", icon = "icon-daily", color = "#FF6B6B"))
            .post("/api/v1/memos/categories")
            .then()
            .status(HttpStatus.CREATED)
            .apply(
                document(
                    "addMemoCategory",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("icon").type(JsonFieldType.STRING).description("카테고리 아이콘"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("카테고리 색상"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("생성된 카테고리 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun modifyCategory() {
        val userId = UUID.randomUUID()
        setupAuth(userId)

        every { memoCategoryService.modify(userId, 1L, any()) } returns 1L

        given()
            .contentType(ContentType.JSON)
            .body(ModifyMemoCategoryRequest(name = "일상 수정", color = "#FF0000"))
            .patch("/api/v1/memos/categories/{id}", 1L)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "modifyMemoCategory",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("카테고리 ID"),
                    ),
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름, null 가능").optional(),
                        fieldWithPath("icon").type(JsonFieldType.STRING).description("카테고리 아이콘, null 가능").optional(),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("카테고리 색상, null 가능").optional(),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("수정된 카테고리 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun removeCategory() {
        val userId = UUID.randomUUID()
        setupAuth(userId)

        every { memoCategoryService.remove(userId, 1L) } returns 1L

        given()
            .contentType(ContentType.JSON)
            .delete("/api/v1/memos/categories/{id}", 1L)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "removeMemoCategory",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("카테고리 ID"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("삭제된 카테고리 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }
}
