package com.benecia.lifetracker.domain.ledger.api

import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.domain.ledger.dto.AddLedgerEntryRequest
import com.benecia.lifetracker.domain.ledger.dto.LedgerEntryResponse
import com.benecia.lifetracker.domain.ledger.dto.LedgerSummaryResponse
import com.benecia.lifetracker.domain.ledger.dto.ModifyLedgerEntryRequest
import com.benecia.lifetracker.ledgercore.service.LedgerEntryService
import com.benecia.lifetracker.security.userdetails.LoginUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/ledger")
class LedgerEntryController(
    private val ledgerEntryService: LedgerEntryService,
) {
    @GetMapping
    fun readEntries(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestParam year: Int,
        @RequestParam month: Int,
    ): ApiResponse<List<LedgerEntryResponse>> {
        val entries = ledgerEntryService.findAllByUserIdAndYearMonth(loginUser.id, year, month)
        return ApiResponse.success(entries.map { LedgerEntryResponse.of(it) })
    }

    @GetMapping("/summary")
    fun readSummary(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestParam year: Int,
        @RequestParam month: Int,
    ): ApiResponse<LedgerSummaryResponse> {
        val summary = ledgerEntryService.getSummary(loginUser.id, year, month)
        return ApiResponse.success(LedgerSummaryResponse.of(summary))
    }

    @PostMapping
    fun addEntry(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestBody request: AddLedgerEntryRequest,
    ): ApiResponse<Long> {
        val id = ledgerEntryService.add(loginUser.id, request.toCommand())
        return ApiResponse.created(id)
    }

    @PatchMapping("/{id}")
    fun modifyEntry(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
        @RequestBody request: ModifyLedgerEntryRequest,
    ): ApiResponse<Long> {
        val entryId = ledgerEntryService.modify(loginUser.id, id, request.toCommand())
        return ApiResponse.success(entryId)
    }

    @DeleteMapping("/{id}")
    fun removeEntry(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
    ): ApiResponse<Long> {
        val removedId = ledgerEntryService.remove(loginUser.id, id)
        return ApiResponse.success(removedId)
    }
}
