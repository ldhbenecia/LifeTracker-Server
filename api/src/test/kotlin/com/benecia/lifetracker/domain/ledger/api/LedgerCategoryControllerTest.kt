package com.benecia.lifetracker.domain.ledger.api

import com.benecia.lifetracker.domain.ledger.dto.AddLedgerCategoryRequest
import com.benecia.lifetracker.domain.ledger.dto.ModifyLedgerCategoryRequest
import com.benecia.lifetracker.ledgercore.model.info.LedgerCategoryInfo
import com.benecia.lifetracker.ledgercore.service.LedgerCategoryService
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.test.api.RestDocsTest
import com.benecia.lifetracker.test.api.RestDocsUtils.requestPreprocessor
import com.benecia.lifetracker.test.api.RestDocsUtils.responsePreprocessor
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

class LedgerCategoryControllerTest : RestDocsTest() {
    private lateinit var ledgerCategoryService: LedgerCategoryService
    private lateinit var controller: LedgerCategoryController

    @BeforeEach
    fun setUp() {
        ledgerCategoryService = mockk()
        controller = LedgerCategoryController(ledgerCategoryService)
        mockMvc = mockController(controller)
    }

    private fun setupAuthentication(loginUser: LoginUser) {
        val auth = UsernamePasswordAuthenticationToken(loginUser, null, loginUser.authorities)
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun createLoginUser(userId: UUID): LoginUser = LoginUser(
        id = userId,
        email = "test@test.com",
        displayName = "테스트유저",
        profileImageUrl = "https://profile.com/img.png",
    )

    @Test
    fun readCategories() {
        val userId = UUID.randomUUID()
        setupAuthentication(createLoginUser(userId))

        val categories = listOf(
            LedgerCategoryInfo(id = 1L, name = "식비", icon = "🍔", color = "#FF5733"),
            LedgerCategoryInfo(id = 2L, name = "교통", icon = "🚇", color = "#3498DB"),
        )
        every { ledgerCategoryService.findAllByUserId(userId) } returns categories

        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/ledger/categories")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "readLedgerCategories",
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
        setupAuthentication(createLoginUser(userId))

        val request = AddLedgerCategoryRequest(name = "식비", icon = "🍔", color = "#FF5733")
        every { ledgerCategoryService.add(userId, request.toCommand()) } returns 1L

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/api/v1/ledger/categories")
            .then()
            .status(HttpStatus.CREATED)
            .apply(
                document(
                    "addLedgerCategory",
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
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("생성된 카테고리 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun modifyCategory() {
        val userId = UUID.randomUUID()
        setupAuthentication(createLoginUser(userId))

        val id = 1L
        val request = ModifyLedgerCategoryRequest(name = "식비", icon = "🍕", color = "#E74C3C")
        every { ledgerCategoryService.modify(userId, id, request.toCommand()) } returns id

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .patch("/api/v1/ledger/categories/{id}", id)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "modifyLedgerCategory",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("수정할 카테고리 ID"),
                    ),
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름").optional(),
                        fieldWithPath("icon").type(JsonFieldType.STRING).description("카테고리 아이콘").optional(),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("카테고리 색상").optional(),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("수정된 카테고리 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun removeCategory() {
        val userId = UUID.randomUUID()
        setupAuthentication(createLoginUser(userId))

        val id = 1L
        every { ledgerCategoryService.remove(userId, id) } returns id

        given()
            .contentType(ContentType.JSON)
            .delete("/api/v1/ledger/categories/{id}", id)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "removeLedgerCategory",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("삭제할 카테고리 ID"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("삭제된 카테고리 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }
}
