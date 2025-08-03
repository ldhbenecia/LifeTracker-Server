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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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

        val todoId = 1L

        // 카테고리 있는 경우
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
            scheduledDate = LocalDate.of(2025, 7, 8),
            scheduledTime = LocalTime.of(20, 0),
            notificationTime = LocalDateTime.of(2025, 7, 8, 18, 0),
            isDone = false,
        )

        every { todoService.findTodoById(userId, todoId) } returns expected

        setupAuthentication(loginUser)
        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/todos/{id}", todoId)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "findTodo_withCategory",
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
                        fieldWithPath("data.category").type(JsonFieldType.OBJECT).description("카테고리 정보, 없을 수 있음").optional(),
                        fieldWithPath("data.category.id").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("data.category.name").type(JsonFieldType.STRING).description("카테고리 이름").optional(),
                        fieldWithPath("data.category.icon").type(JsonFieldType.STRING).description("카테고리 아이콘").optional(),
                        fieldWithPath("data.category.color").type(JsonFieldType.STRING).description("카테고리 색상").optional(),
                        fieldWithPath("data.scheduledDate").type(JsonFieldType.STRING).description("예약 날짜 (yyyy-MM-dd)"),
                        fieldWithPath("data.scheduledTime").type(JsonFieldType.STRING).description("예약 시간 (HH:mm:ss), null 가능").optional(),
                        fieldWithPath("data.notificationTime").type(JsonFieldType.STRING).description("알림 시간 (ISO-8601), null 가능").optional(),
                        fieldWithPath("data.isDone").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )

        // 카테고리 없는 경우
        val expectedNoCategory = TodoInfo(
            id = todoId + 1,
            title = "서버 개발",
            category = null,
            scheduledDate = LocalDate.of(2025, 7, 8),
            scheduledTime = LocalTime.of(20, 0),
            notificationTime = LocalDateTime.of(2025, 7, 8, 18, 0),
            isDone = false,
        )
        every { todoService.findTodoById(userId, todoId + 1) } returns expectedNoCategory

        setupAuthentication(loginUser)
        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/todos/{id}", todoId + 1)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "findTodo_noCategory",
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
                        fieldWithPath("data.category").type(JsonFieldType.OBJECT).description("카테고리 정보, 없을 수 있음").optional(),
                        fieldWithPath("data.category.id").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("data.category.name").type(JsonFieldType.STRING).description("카테고리 이름").optional(),
                        fieldWithPath("data.category.icon").type(JsonFieldType.STRING).description("카테고리 아이콘").optional(),
                        fieldWithPath("data.category.color").type(JsonFieldType.STRING).description("카테고리 색상").optional(),
                        fieldWithPath("data.scheduledDate").type(JsonFieldType.STRING).description("예약 날짜 (yyyy-MM-dd)"),
                        fieldWithPath("data.scheduledTime").type(JsonFieldType.STRING).description("예약 시간 (HH:mm:ss), null 가능").optional(),
                        fieldWithPath("data.notificationTime").type(JsonFieldType.STRING).description("알림 시간 (ISO-8601), null 가능").optional(),
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

        // 카테고리 있는 투두
        val categoryInfo = CategoryInfo(
            id = 1L,
            name = "개발",
            icon = "icon-dev",
            color = "#FF0000",
        )

        val todoListWithCategory = listOf(
            TodoInfo(
                id = 1L,
                title = "서버 개발",
                category = categoryInfo,
                scheduledDate = LocalDate.of(2025, 7, 8),
                scheduledTime = LocalTime.of(20, 0),
                notificationTime = LocalDateTime.of(2025, 7, 8, 18, 0),
                isDone = false,
            ),
        )

        every { todoService.findTodosByMonth(userId, 2025, 7) } returns todoListWithCategory

        setupAuthentication(loginUser)
        given()
            .contentType(ContentType.JSON)
            .queryParam("year", 2025)
            .queryParam("month", 7)
            .get("/api/v1/todos/month")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "findTodosByMonth_withCategory",
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
                        fieldWithPath("data[].category").type(JsonFieldType.OBJECT).description("카테고리 정보, 없을 수 있음").optional(),
                        fieldWithPath("data[].category.id").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("data[].category.name").type(JsonFieldType.STRING).description("카테고리 이름").optional(),
                        fieldWithPath("data[].category.icon").type(JsonFieldType.STRING).description("카테고리 아이콘").optional(),
                        fieldWithPath("data[].category.color").type(JsonFieldType.STRING).description("카테고리 색상").optional(),
                        fieldWithPath("data[].scheduledDate").type(JsonFieldType.STRING).description("예약 날짜 (yyyy-MM-dd)"),
                        fieldWithPath("data[].scheduledTime").type(JsonFieldType.STRING).description("예약 시간 (HH:mm:ss), null 가능").optional(),
                        fieldWithPath("data[].notificationTime").type(JsonFieldType.STRING).description("알림 시간 (ISO-8601), null 가능").optional(),
                        fieldWithPath("data[].isDone").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )

        // 카테고리 없는 투두
        val todoListNoCategory = listOf(
            TodoInfo(
                id = 2L,
                title = "운동하기",
                category = null,
                scheduledDate = LocalDate.of(2025, 7, 9),
                scheduledTime = LocalTime.of(10, 0),
                notificationTime = LocalDateTime.of(2025, 7, 9, 8, 0),
                isDone = false,
            ),
        )
        every { todoService.findTodosByMonth(userId, 2025, 8) } returns todoListNoCategory

        setupAuthentication(loginUser)
        given()
            .contentType(ContentType.JSON)
            .queryParam("year", 2025)
            .queryParam("month", 8)
            .get("/api/v1/todos/month")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "findTodosByMonth_noCategory",
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
                        fieldWithPath("data[].category").type(JsonFieldType.OBJECT).description("카테고리 정보, 없을 수 있음").optional(),
                        fieldWithPath("data[].category.id").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("data[].category.name").type(JsonFieldType.STRING).description("카테고리 이름").optional(),
                        fieldWithPath("data[].category.icon").type(JsonFieldType.STRING).description("카테고리 아이콘").optional(),
                        fieldWithPath("data[].category.color").type(JsonFieldType.STRING).description("카테고리 색상").optional(),
                        fieldWithPath("data[].scheduledDate").type(JsonFieldType.STRING).description("예약 날짜 (yyyy-MM-dd)"),
                        fieldWithPath("data[].scheduledTime").type(JsonFieldType.STRING).description("예약 시간 (HH:mm:ss), null 가능").optional(),
                        fieldWithPath("data[].notificationTime").type(JsonFieldType.STRING).description("알림 시간 (ISO-8601), null 가능").optional(),
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

        every { todoService.addTodo(userId, capture(commandSlot)) } returns 1L

        // 카테고리 없는 경우
        val requestBodyNoCategory = mapOf(
            "title" to "서버 개발",
            "categoryId" to null,
            "scheduledDate" to "2025-07-08",
            "scheduledTime" to "21:00:00",
            "notificationTime" to "2025-07-08T18:00:00",
        )

        setupAuthentication(loginUser)
        given()
            .contentType(ContentType.JSON)
            .body(requestBodyNoCategory)
            .post("/api/v1/todos")
            .then()
            .status(HttpStatus.CREATED)
            .apply(
                document(
                    "addTodo_noCategory",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("Todo 제목"),
                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("scheduledDate").type(JsonFieldType.STRING).description("예약 날짜 (yyyy-MM-dd)"),
                        fieldWithPath("scheduledTime").type(JsonFieldType.STRING).description("예약 시간 (HH:mm:ss), null 가능").optional(),
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

        // 카테고리 있는 경우
        val requestBodyWithCategory = mapOf(
            "title" to "서버 개발",
            "categoryId" to 2L,
            "scheduledDate" to "2025-07-08",
            "scheduledTime" to "21:00:00",
            "notificationTime" to "2025-07-08T18:00:00",
        )

        setupAuthentication(loginUser)
        given()
            .contentType(ContentType.JSON)
            .body(requestBodyWithCategory)
            .post("/api/v1/todos")
            .then()
            .status(HttpStatus.CREATED)
            .apply(
                document(
                    "addTodo_withCategory",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("Todo 제목"),
                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("scheduledDate").type(JsonFieldType.STRING).description("예약 날짜 (yyyy-MM-dd)"),
                        fieldWithPath("scheduledTime").type(JsonFieldType.STRING).description("예약 시간 (HH:mm:ss), null 가능").optional(),
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

        val todoId = 1L

        every { todoService.modifyTodo(userId, todoId, capture(commandSlot)) } returns todoId

        // 카테고리 없는 경우
        val requestBodyNoCategory = mapOf(
            "title" to "서버 개발 수정",
            "categoryId" to null,
            "scheduledDate" to "2025-07-08",
            "scheduledTime" to "21:00:00",
            "notificationTime" to "2025-07-08T19:00:00",
            "isDone" to true,
        )

        setupAuthentication(loginUser)
        given()
            .contentType(ContentType.JSON)
            .body(requestBodyNoCategory)
            .patch("/api/v1/todos/{id}", todoId)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "modifyTodo_noCategory",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("Todo ID"),
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("Todo 제목"),
                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("scheduledDate").type(JsonFieldType.STRING).description("예약 날짜 (yyyy-MM-dd)"),
                        fieldWithPath("scheduledTime").type(JsonFieldType.STRING).description("예약 시간 (HH:mm:ss), null 가능").optional(),
                        fieldWithPath("notificationTime").type(JsonFieldType.STRING).description("알림 시간 (ISO-8601), null 가능").optional(),
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

        // 카테고리 있는 경우
        val requestBodyWithCategory = mapOf(
            "title" to "서버 개발 수정",
            "categoryId" to 2L,
            "scheduledDate" to "2025-07-08",
            "scheduledTime" to "21:00:00",
            "notificationTime" to "2025-07-08T19:00:00",
            "isDone" to true,
        )

        setupAuthentication(loginUser)
        given()
            .contentType(ContentType.JSON)
            .body(requestBodyWithCategory)
            .patch("/api/v1/todos/{id}", todoId)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "modifyTodo_withCategory",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("Todo ID"),
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("Todo 제목"),
                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("scheduledDate").type(JsonFieldType.STRING).description("예약 날짜 (yyyy-MM-dd)"),
                        fieldWithPath("scheduledTime").type(JsonFieldType.STRING).description("예약 시간 (HH:mm:ss), null 가능").optional(),
                        fieldWithPath("notificationTime").type(JsonFieldType.STRING).description("알림 시간 (ISO-8601), null 가능").optional(),
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

    @Test
    fun markDone() {
        val userId = UUID.randomUUID()
        val email = "test@test.com"
        val loginUser = LoginUser(userId, email)

        val todoId = 1L
        val done = true

        every { todoService.markDone(userId, todoId, done) } returns todoId

        setupAuthentication(loginUser)
        given()
            .contentType(ContentType.JSON)
            .queryParam("done", done)
            .patch("/api/v1/todos/{id}/done", todoId)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "markDone",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("Todo ID"),
                    ),
                    queryParameters(
                        parameterWithName("done").description("완료 여부 (true: 완료, false: 미완료)"),
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

    @Test
    fun removeTodo() {
        val userId = UUID.randomUUID()
        val email = "test@test.com"
        val loginUser = LoginUser(userId, email)

        val todoId = 1L

        every { todoService.removeTodo(userId, todoId) } returns todoId

        setupAuthentication(loginUser)
        given()
            .contentType(ContentType.JSON)
            .delete("/api/v1/todos/{id}", todoId)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "removeTodo",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("Todo ID"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("삭제된 할 일 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }
}
