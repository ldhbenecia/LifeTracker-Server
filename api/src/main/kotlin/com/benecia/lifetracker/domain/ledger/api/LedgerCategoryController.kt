package com.benecia.lifetracker.domain.ledger.api

import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.domain.ledger.dto.AddLedgerCategoryRequest
import com.benecia.lifetracker.domain.ledger.dto.LedgerCategoryResponse
import com.benecia.lifetracker.domain.ledger.dto.ModifyLedgerCategoryRequest
import com.benecia.lifetracker.ledgercore.service.LedgerCategoryService
import com.benecia.lifetracker.security.userdetails.LoginUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/ledger/categories")
class LedgerCategoryController(
    private val ledgerCategoryService: LedgerCategoryService,
) {
    @GetMapping
    fun readCategories(
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ApiResponse<List<LedgerCategoryResponse>> {
        val categories = ledgerCategoryService.findAllByUserId(loginUser.id)
        return ApiResponse.success(categories.map { LedgerCategoryResponse.of(it) })
    }

    @PostMapping
    fun addCategory(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestBody request: AddLedgerCategoryRequest,
    ): ApiResponse<Long> {
        val id = ledgerCategoryService.add(loginUser.id, request.toCommand())
        return ApiResponse.created(id)
    }

    @PatchMapping("/{id}")
    fun modifyCategory(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
        @RequestBody request: ModifyLedgerCategoryRequest,
    ): ApiResponse<Long> {
        val categoryId = ledgerCategoryService.modify(loginUser.id, id, request.toCommand())
        return ApiResponse.success(categoryId)
    }

    @DeleteMapping("/{id}")
    fun removeCategory(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
    ): ApiResponse<Long> {
        val removedId = ledgerCategoryService.remove(loginUser.id, id)
        return ApiResponse.success(removedId)
    }
}
