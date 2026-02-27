package com.benecia.lifetracker.domain.ledger.api

import com.benecia.lifetracker.domain.ledger.dto.AddLedgerEntryRequest
import com.benecia.lifetracker.domain.ledger.dto.ModifyLedgerEntryRequest
import com.benecia.lifetracker.ledgercore.model.info.CategorySummary
import com.benecia.lifetracker.ledgercore.model.info.LedgerEntryInfo
import com.benecia.lifetracker.ledgercore.model.info.LedgerSummaryInfo
import com.benecia.lifetracker.ledgercore.model.info.PaymentMethodSummary
import com.benecia.lifetracker.ledgercore.service.LedgerEntryService
import com.benecia.lifetracker.ledgercore.service.LedgerType
import com.benecia.lifetracker.ledgercore.service.PaymentMethod
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.test.api.RestDocsTest
import com.benecia.lifetracker.test.api.RestDocsUtils.requestPreprocessor
import com.benecia.lifetracker.test.api.RestDocsUtils.responsePreprocessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
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
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.time.LocalDate
import java.util.UUID

class LedgerEntryControllerTest : RestDocsTest() {
    private lateinit var ledgerEntryService: LedgerEntryService
    private lateinit var controller: LedgerEntryController
    private val objectMapper = ObjectMapper()
        .findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    @BeforeEach
    fun setUp() {
        ledgerEntryService = mockk()
        controller = LedgerEntryController(ledgerEntryService)
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
    fun readEntries() {
        val userId = UUID.randomUUID()
        setupAuthentication(createLoginUser(userId))

        val entries = listOf(
            LedgerEntryInfo(
                id = 1L,
                type = LedgerType.EXPENSE,
                amount = 15000L,
                paymentMethod = PaymentMethod.CREDIT_CARD,
                categoryId = 1L,
                memo = "점심",
                transactionDate = LocalDate.of(2026, 2, 15),
            ),
        )
        every { ledgerEntryService.findAllByUserIdAndYearMonth(userId, 2026, 2) } returns entries

        given()
            .contentType(ContentType.JSON)
            .queryParam("year", 2026)
            .queryParam("month", 2)
            .get("/api/v1/ledger")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "readLedgerEntries",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    queryParameters(
                        parameterWithName("year").description("조회 연도"),
                        parameterWithName("month").description("조회 월"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("항목 ID"),
                        fieldWithPath("data[].type").type(JsonFieldType.STRING).description("유형 (INCOME/EXPENSE)"),
                        fieldWithPath("data[].amount").type(JsonFieldType.NUMBER).description("금액"),
                        fieldWithPath("data[].paymentMethod").type(JsonFieldType.STRING).description("결제 수단"),
                        fieldWithPath("data[].categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("data[].memo").type(JsonFieldType.STRING).description("메모").optional(),
                        fieldWithPath("data[].transactionDate").type(JsonFieldType.STRING).description("거래 날짜"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun readSummary() {
        val userId = UUID.randomUUID()
        setupAuthentication(createLoginUser(userId))

        val summary = LedgerSummaryInfo(
            totalIncome = 3000000L,
            totalExpense = 1500000L,
            netAmount = 1500000L,
            categoryBreakdown = listOf(
                CategorySummary(categoryId = 1L, totalAmount = 500000L, count = 10),
            ),
            paymentMethodBreakdown = listOf(
                PaymentMethodSummary(paymentMethod = PaymentMethod.CREDIT_CARD, totalAmount = 1000000L, count = 8),
            ),
        )
        every { ledgerEntryService.getSummary(userId, 2026, 2) } returns summary

        given()
            .contentType(ContentType.JSON)
            .queryParam("year", 2026)
            .queryParam("month", 2)
            .get("/api/v1/ledger/summary")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "readLedgerSummary",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    queryParameters(
                        parameterWithName("year").description("조회 연도"),
                        parameterWithName("month").description("조회 월"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.totalIncome").type(JsonFieldType.NUMBER).description("총 수입"),
                        fieldWithPath("data.totalExpense").type(JsonFieldType.NUMBER).description("총 지출"),
                        fieldWithPath("data.netAmount").type(JsonFieldType.NUMBER).description("순액 (수입 - 지출)"),
                        fieldWithPath("data.categoryBreakdown[].categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("data.categoryBreakdown[].totalAmount").type(JsonFieldType.NUMBER).description("카테고리별 합계"),
                        fieldWithPath("data.categoryBreakdown[].count").type(JsonFieldType.NUMBER).description("카테고리별 건수"),
                        fieldWithPath("data.paymentMethodBreakdown[].paymentMethod").type(JsonFieldType.STRING).description("결제 수단"),
                        fieldWithPath("data.paymentMethodBreakdown[].totalAmount").type(JsonFieldType.NUMBER).description("결제수단별 합계"),
                        fieldWithPath("data.paymentMethodBreakdown[].count").type(JsonFieldType.NUMBER).description("결제수단별 건수"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun addEntry() {
        val userId = UUID.randomUUID()
        setupAuthentication(createLoginUser(userId))

        val request = AddLedgerEntryRequest(
            type = LedgerType.EXPENSE,
            amount = 15000L,
            paymentMethod = PaymentMethod.CREDIT_CARD,
            categoryId = 1L,
            memo = "점심",
            transactionDate = LocalDate.of(2026, 2, 15),
        )
        every { ledgerEntryService.add(userId, request.toCommand()) } returns 1L

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(request))
            .post("/api/v1/ledger")
            .then()
            .status(HttpStatus.CREATED)
            .apply(
                document(
                    "addLedgerEntry",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("type").type(JsonFieldType.STRING).description("유형 (INCOME/EXPENSE)"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("금액"),
                        fieldWithPath("paymentMethod").type(JsonFieldType.STRING).description("결제 수단 (CREDIT_CARD/DEBIT_CARD/CASH/TRANSFER)"),
                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("memo").type(JsonFieldType.STRING).description("메모").optional(),
                        fieldWithPath("transactionDate").type(JsonFieldType.STRING).description("거래 날짜 (yyyy-MM-dd)"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("생성된 항목 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun modifyEntry() {
        val userId = UUID.randomUUID()
        setupAuthentication(createLoginUser(userId))

        val id = 1L
        val request = ModifyLedgerEntryRequest(
            type = null,
            amount = 20000L,
            paymentMethod = null,
            categoryId = null,
            categoryIdUpdated = false,
            memo = "저녁",
            memoUpdated = true,
            transactionDate = null,
        )
        every { ledgerEntryService.modify(userId, id, request.toCommand()) } returns id

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/v1/ledger/{id}", id)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "modifyLedgerEntry",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("수정할 항목 ID"),
                    ),
                    requestFields(
                        fieldWithPath("type").type(JsonFieldType.STRING).description("유형 (INCOME/EXPENSE)").optional(),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("금액").optional(),
                        fieldWithPath("paymentMethod").type(JsonFieldType.STRING).description("결제 수단").optional(),
                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID").optional(),
                        fieldWithPath("categoryIdUpdated").type(JsonFieldType.BOOLEAN).description("카테고리 ID 변경 여부 (null로 변경 시 true)"),
                        fieldWithPath("memo").type(JsonFieldType.STRING).description("메모").optional(),
                        fieldWithPath("memoUpdated").type(JsonFieldType.BOOLEAN).description("메모 변경 여부 (null로 변경 시 true)"),
                        fieldWithPath("transactionDate").type(JsonFieldType.STRING).description("거래 날짜").optional(),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("수정된 항목 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }

    @Test
    fun removeEntry() {
        val userId = UUID.randomUUID()
        setupAuthentication(createLoginUser(userId))

        val id = 1L
        every { ledgerEntryService.remove(userId, id) } returns id

        given()
            .contentType(ContentType.JSON)
            .delete("/api/v1/ledger/{id}", id)
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "removeLedgerEntry",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    pathParameters(
                        parameterWithName("id").description("삭제할 항목 ID"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("삭제된 항목 ID"),
                        fieldWithPath("timestamp").type(JsonFieldType.STRING).description("응답 생성 시간"),
                    ),
                ),
            )
    }
}
