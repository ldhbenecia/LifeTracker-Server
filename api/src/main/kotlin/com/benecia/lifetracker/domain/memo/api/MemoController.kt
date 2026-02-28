package com.benecia.lifetracker.domain.memo.api

import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.domain.memo.dto.AddMemoRequest
import com.benecia.lifetracker.domain.memo.dto.DefaultMemoResponse
import com.benecia.lifetracker.domain.memo.dto.MemoResponse
import com.benecia.lifetracker.domain.memo.dto.ModifyMemoRequest
import com.benecia.lifetracker.memocore.service.MemoService
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
@RequestMapping("/api/v1/memos")
class MemoController(
    private val memoService: MemoService,
) {
    @GetMapping
    fun readMemos(
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ApiResponse<List<MemoResponse>> {
        val memos = memoService.findAll(loginUser.id)
        return ApiResponse.success(memos.map { MemoResponse.of(it) })
    }

    @PostMapping
    fun addMemo(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestBody request: AddMemoRequest,
    ): ApiResponse<DefaultMemoResponse> {
        val id = memoService.add(loginUser.id, request.toCommand())
        return ApiResponse.created(DefaultMemoResponse(id))
    }

    @PatchMapping("/{id}")
    fun modifyMemo(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
        @RequestBody request: ModifyMemoRequest,
    ): ApiResponse<DefaultMemoResponse> {
        val resultId = memoService.modify(loginUser.id, id, request.toCommand())
        return ApiResponse.success(DefaultMemoResponse(resultId))
    }

    @DeleteMapping("/{id}")
    fun removeMemo(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
    ): ApiResponse<DefaultMemoResponse> {
        val resultId = memoService.remove(loginUser.id, id)
        return ApiResponse.success(DefaultMemoResponse(resultId))
    }
}
