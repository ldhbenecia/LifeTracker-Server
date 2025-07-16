package com.benecia.lifetracker.domain.todo.api

import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.test.api.RestDocsTest
import com.benecia.lifetracker.test.api.RestDocsUtils.requestPreprocessor
import com.benecia.lifetracker.test.api.RestDocsUtils.responsePreprocessor
import com.benecia.lifetracker.todocore.model.command.ModifyTodo
import com.benecia.lifetracker.todocore.model.command.NewTodo
import com.benecia.lifetracker.todocore.model.info.CategoryInfo
import com.benecia.lifetracker.todocore.model.info.TodoInfo
import com.benecia.lifetracker.todocore.service.TodoService
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
import java.time.LocalDateTime
import java.util.UUID

class TodoControllerTest : RestDocsTest() {
    private lateinit var todoService: TodoService
    private lateinit var controller: TodoController

    @BeforeEach
    fun setUp() {
        todoService = mockk()
        controller = TodoController(todoService)
        mockMvc = mockController(controller)
    }

    private fun setupAuthentication(loginUser: LoginUser) {
        val auth = UsernamePasswordAuthenticationToken(loginUser, null, loginUser.authorities)
        SecurityContextHolder.getContext().authentication = auth
    }

    @Test
    fun findTodo() {
        val userId = UUID.randomUUID()
        val email = "test@test.com"
        val loginUser = LoginUser(userId, email)

        setupAuthentication(loginUser)

        val todoId = 1L

        val categoryInfo = CategoryInfo(
            id = 1L,
            name = "개발",
            icon = "icon-dev",
            color = "#FF0000",
        )

        val expected = TodoInfo(
            id = todoId,
            title = "서버 개발",
            category = categoryInfo,
            scheduledDate = LocalDateTime.of(2025, 7, 8, 20, 0),
            notificationTime = LocalDateTime.of(2025, 7, 8, 18, 0),
            isDone = false,
        )

        every { todoService.findTodoById(userId, todoId) } returns expected

        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/todos/{id}", todoId)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "findTodo",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("Todo ID"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("할 일 ID"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("할 일 제목"),
                        fieldWithPath("data.category.id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("data.category.name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("data.category.icon").type(JsonFieldType.STRING).description("카테고리 아이콘"),
                        fieldWithPath("data.category.color").type(JsonFieldType.STRING).description("카테고리 색상"),
                        fieldWithPath("data.scheduledDate").type(JsonFieldType.STRING).description("예약 날짜 및 시간 (ISO-8601)"),
                        fieldWithPath("data.notificationTime").type(JsonFieldType.STRING).description("알림 시간 (ISO-8601)"),
                        fieldWithPath("data.isDone").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun findTodosByMonth() {
        val userId = UUID.randomUUID()
        val email = "test@test.com"
        val loginUser = LoginUser(userId, email)

        setupAuthentication(loginUser)

        val categoryInfo = CategoryInfo(
            id = 1L,
            name = "개발",
            icon = "icon-dev",
            color = "#FF0000",
        )
        val todoList = listOf(
            TodoInfo(
                id = 1L,
                title = "서버 개발",
                category = categoryInfo,
                scheduledDate = LocalDateTime.of(2025, 7, 8, 20, 0),
                notificationTime = LocalDateTime.of(2025, 7, 8, 18, 0),
                isDone = false,
            ),
        )

        every { todoService.findTodosByMonth(userId, 2025, 7) } returns todoList

        given()
            .contentType(ContentType.JSON)
            .queryParam("year", 2025)
            .queryParam("month", 7)
            .get("/api/v1/todos/month")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "findTodosByMonth",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    queryParameters(
                        parameterWithName("year").description("조회 연도"),
                        parameterWithName("month").description("조회 월"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("할 일 ID"),
                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("할 일 제목"),
                        fieldWithPath("data[].category.id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("data[].category.name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("data[].category.icon").type(JsonFieldType.STRING).description("카테고리 아이콘"),
                        fieldWithPath("data[].category.color").type(JsonFieldType.STRING).description("카테고리 색상"),
                        fieldWithPath("data[].scheduledDate").type(JsonFieldType.STRING).description("예약 날짜 및 시간 (ISO-8601)"),
                        fieldWithPath("data[].notificationTime").type(JsonFieldType.STRING).description("알림 시간 (ISO-8601)"),
                        fieldWithPath("data[].isDone").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun addTodo() {
        val commandSlot = slot<NewTodo>()
        val userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        val email = "test@test.com"
        val loginUser = LoginUser(userId, email)

        setupAuthentication(loginUser)

        every { todoService.addTodo(userId, capture(commandSlot)) } returns 1L

        val requestBody = mapOf(
            "title" to "서버 개발",
            "category" to "개발",
            "scheduledDate" to "2025-07-08T20:00:00",
            "notificationTime" to "2025-07-08T18:00:00",
        )

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .post("/api/v1/todos")
            .then()
            .status(HttpStatus.CREATED)
            .apply(
                document(
                    "addTodo",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("Todo 제목"),
                        fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리"),
                        fieldWithPath("scheduledDate").type(JsonFieldType.STRING).description("예약된 날짜 및 시간 (ISO8601)"),
                        fieldWithPath("notificationTime").type(JsonFieldType.STRING)
                            .description("알림 시간 (ISO8601), null 가능").optional(),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("생성된 할 일 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun modifyTodo() {
        val commandSlot = slot<ModifyTodo>()
        val userId = UUID.randomUUID()
        val email = "test@test.com"
        val loginUser = LoginUser(userId, email)

        setupAuthentication(loginUser)

        val todoId = 1L

        every { todoService.modifyTodo(userId, todoId, capture(commandSlot)) } returns todoId

        val requestBody = mapOf(
            "title" to "서버 개발 수정",
            "category" to "개발",
            "scheduledDate" to "2025-07-08T21:00:00",
            "notificationTime" to "2025-07-08T19:00:00",
            "isDone" to true,
        )

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .put("/api/v1/todos/{id}", todoId)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "modifyTodo",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("Todo ID"),
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("Todo 제목"),
                        fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리"),
                        fieldWithPath("scheduledDate").type(JsonFieldType.STRING).description("예약된 날짜 및 시간 (ISO8601)"),
                        fieldWithPath("notificationTime").type(JsonFieldType.STRING).description("알림 시간 (ISO8601), null 가능").optional(),
                        fieldWithPath("isDone").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("수정된 할 일 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }
}
