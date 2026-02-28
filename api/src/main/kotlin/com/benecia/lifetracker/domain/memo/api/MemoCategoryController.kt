package com.benecia.lifetracker.domain.memo.api

import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.domain.memo.dto.AddMemoCategoryRequest
import com.benecia.lifetracker.domain.memo.dto.DefaultMemoResponse
import com.benecia.lifetracker.domain.memo.dto.MemoCategoryResponse
import com.benecia.lifetracker.domain.memo.dto.ModifyMemoCategoryRequest
import com.benecia.lifetracker.memocore.service.MemoCategoryService
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
@RequestMapping("/api/v1/memos/categories")
class MemoCategoryController(
    private val memoCategoryService: MemoCategoryService,
) {
    @GetMapping
    fun readCategories(
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ApiResponse<List<MemoCategoryResponse>> {
        val categories = memoCategoryService.findAll(loginUser.id)
        return ApiResponse.success(categories.map { MemoCategoryResponse.of(it) })
    }

    @PostMapping
    fun addCategory(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestBody request: AddMemoCategoryRequest,
    ): ApiResponse<DefaultMemoResponse> {
        val id = memoCategoryService.add(loginUser.id, request.toCommand())
        return ApiResponse.created(DefaultMemoResponse(id))
    }

    @PatchMapping("/{id}")
    fun modifyCategory(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
        @RequestBody request: ModifyMemoCategoryRequest,
    ): ApiResponse<DefaultMemoResponse> {
        val resultId = memoCategoryService.modify(loginUser.id, id, request.toCommand())
        return ApiResponse.success(DefaultMemoResponse(resultId))
    }

    @DeleteMapping("/{id}")
    fun removeCategory(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
    ): ApiResponse<DefaultMemoResponse> {
        val resultId = memoCategoryService.remove(loginUser.id, id)
        return ApiResponse.success(DefaultMemoResponse(resultId))
    }
}
