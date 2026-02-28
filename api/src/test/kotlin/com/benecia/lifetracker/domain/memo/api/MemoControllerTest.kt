package com.benecia.lifetracker.domain.memo.api

import com.benecia.lifetracker.domain.memo.dto.AddMemoRequest
import com.benecia.lifetracker.domain.memo.dto.ModifyMemoRequest
import com.benecia.lifetracker.memocore.model.info.MemoCategoryInfo
import com.benecia.lifetracker.memocore.model.info.MemoInfo
import com.benecia.lifetracker.memocore.service.MemoService
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.test.api.RestDocsTest
import com.benecia.lifetracker.test.api.RestDocsUtils.requestPreprocessor
import com.benecia.lifetracker.test.api.RestDocsUtils.responsePreprocessor
import com.fasterxml.jackson.databind.ObjectMapper
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
class MemoControllerTest : RestDocsTest() {

    private lateinit var memoService: MemoService
    private lateinit var controller: MemoController

    private val objectMapper = ObjectMapper().findAndRegisterModules()

    @BeforeEach
    fun setUp() {
        memoService = mockk()
        controller = MemoController(memoService)
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

    private val category = MemoCategoryInfo(id = 1L, name = "일상", icon = "icon-daily", color = "#FF6B6B")

    @Test
    fun readMemos() {
        val userId = UUID.randomUUID()
        setupAuth(userId)

        every { memoService.findAll(userId) } returns listOf(
            MemoInfo(id = 1L, title = "오늘의 메모", content = "내용입니다.", category = category, tags = listOf("태그1", "태그2")),
            MemoInfo(id = 2L, title = "카테고리 없는 메모", content = null, category = null, tags = emptyList()),
        )

        given()
            .contentType(ContentType.JSON)
            .get("/api/v1/memos")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "readMemos",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("메모 ID"),
                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("메모 제목"),
                        fieldWithPath("data[].content").type(JsonFieldType.STRING).description("메모 내용, null 가능").optional(),
                        fieldWithPath("data[].category").type(JsonFieldType.OBJECT).description("카테고리 정보, null 가능").optional(),
                        fieldWithPath("data[].category.id").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("data[].category.name").type(JsonFieldType.STRING).description("카테고리 이름").optional(),
                        fieldWithPath("data[].category.icon").type(JsonFieldType.STRING).description("카테고리 아이콘").optional(),
                        fieldWithPath("data[].category.color").type(JsonFieldType.STRING).description("카테고리 색상").optional(),
                        fieldWithPath("data[].tags").type(JsonFieldType.ARRAY).description("태그 목록"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun addMemo() {
        val userId = UUID.randomUUID()
        setupAuth(userId)

        every { memoService.add(userId, any()) } returns 1L

        val request = AddMemoRequest(
            title = "오늘의 메모",
            content = "내용입니다.",
            categoryId = 1L,
            tags = listOf("태그1", "태그2"),
        )

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(request))
            .post("/api/v1/memos")
            .then()
            .status(HttpStatus.CREATED)
            .apply(
                document(
                    "addMemo",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("메모 제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("메모 내용, null 가능").optional(),
                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID, null 가능").optional(),
                        fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그 목록"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("생성된 메모 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun modifyMemo() {
        val userId = UUID.randomUUID()
        setupAuth(userId)

        every { memoService.modify(userId, 1L, any()) } returns 1L

        val request = ModifyMemoRequest(
            title = "수정된 메모",
            content = "수정된 내용",
            categoryId = null,
            categoryIdUpdated = true,
            tags = listOf("새태그"),
        )

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/v1/memos/{id}", 1L)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "modifyMemo",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("메모 ID"),
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("메모 제목, null 가능").optional(),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("메모 내용, null 가능").optional(),
                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID, null 가능").optional(),
                        fieldWithPath("categoryIdUpdated").type(JsonFieldType.BOOLEAN).description("카테고리 변경 여부 (true면 categoryId 값으로 교체)"),
                        fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그 목록. null=유지, 빈 배열=전체삭제, 값=교체").optional(),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("수정된 메모 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun removeMemo() {
        val userId = UUID.randomUUID()
        setupAuth(userId)

        every { memoService.remove(userId, 1L) } returns 1L

        given()
            .contentType(ContentType.JSON)
            .delete("/api/v1/memos/{id}", 1L)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "removeMemo",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("메모 ID"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("삭제된 메모 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }
}
