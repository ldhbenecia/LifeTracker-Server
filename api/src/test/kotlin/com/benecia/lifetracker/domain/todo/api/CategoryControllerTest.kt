package com.benecia.lifetracker.domain.todo.api

import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.test.api.RestDocsTest
import com.benecia.lifetracker.test.api.RestDocsUtils.requestPreprocessor
import com.benecia.lifetracker.test.api.RestDocsUtils.responsePreprocessor
import com.benecia.lifetracker.todocore.model.command.AddCategory
import com.benecia.lifetracker.todocore.model.command.ModifyCategory
import com.benecia.lifetracker.todocore.model.info.CategoryInfo
import com.benecia.lifetracker.todocore.service.CategoryService
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
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
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.UUID

class CategoryControllerTest : RestDocsTest() {
    private lateinit var categoryService: CategoryService
    private lateinit var controller: CategoryController

    @BeforeEach()
    fun setUp() {
        categoryService = mockk()
        controller = CategoryController(categoryService)
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
    fun readCategories() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        val categories = listOf(
            CategoryInfo(1L, "개발", "icon-dev", "#FF0000"),
            CategoryInfo(2L, "공부", "icon-study", "#00FF00"),
        )
        every { categoryService.findAllByUserId(userId) } returns categories

        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/categories")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "readCategories",
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
    fun readCategory() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        val categoryInfo = CategoryInfo(1L, "개발", "icon-dev", "#FF0000")
        every { categoryService.findById(userId, 1L) } returns categoryInfo

        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/categories/{id}", 1L)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "readCategory",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("카테고리 ID"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("data.icon").type(JsonFieldType.STRING).description("카테고리 아이콘"),
                        fieldWithPath("data.color").type(JsonFieldType.STRING).description("카테고리 색상"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun getCategoryByName() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        val categoryInfo = CategoryInfo(1L, "개발", "icon-dev", "#FF0000")
        every { categoryService.findByName(userId, "개발") } returns categoryInfo

        given()
            .contentType(ContentType.JSON)
            .queryParam("name", "개발")
            .get("/api/v1/categories/search")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "getCategoryByName",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    queryParameters(
                        parameterWithName("name").description("카테고리 이름"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("data.icon").type(JsonFieldType.STRING).description("카테고리 아이콘"),
                        fieldWithPath("data.color").type(JsonFieldType.STRING).description("카테고리 색상"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun addCategory() {
        val userId = UUID.randomUUID()
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        val commandSlot = slot<AddCategory>()
        every { categoryService.add(userId, capture(commandSlot)) } returns 3L

        val requestBody = mapOf(
            "name" to "운동",
            "icon" to "icon-sport",
            "color" to "#0000FF",
        )

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .post("/api/v1/categories")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "addCategory",
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
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        val commandSlot = slot<ModifyCategory>()
        every { categoryService.modify(userId, 1L, capture(commandSlot)) } returns 1L

        val requestBody = mapOf(
            "name" to "수정된 카테고리",
            "icon" to "icon-updated",
            "color" to "#123456",
        )

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .patch("/api/v1/categories/{id}", 1L)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "modifyCategory",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("카테고리 ID"),
                    ),
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("icon").type(JsonFieldType.STRING).description("카테고리 아이콘"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("카테고리 색상"),
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
        val loginUser = createLoginUser(userId)

        setupAuthentication(loginUser)

        every { categoryService.remove(userId, 1L) } returns 1L

        given()
            .contentType(ContentType.JSON)
            .delete("/api/v1/categories/{id}", 1L)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "removeCategory",
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
