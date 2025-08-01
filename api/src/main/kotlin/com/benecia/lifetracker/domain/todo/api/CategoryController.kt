package com.benecia.lifetracker.domain.todo.api

import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.domain.todo.dto.AddCategoryRequest
import com.benecia.lifetracker.domain.todo.dto.AddCategoryResponse
import com.benecia.lifetracker.domain.todo.dto.CategoryResponse
import com.benecia.lifetracker.domain.todo.dto.DeleteCategoryResponse
import com.benecia.lifetracker.domain.todo.dto.ModifyCategoryRequest
import com.benecia.lifetracker.domain.todo.dto.ModifyCategoryResponse
import com.benecia.lifetracker.security.userdetails.LoginUser
import com.benecia.lifetracker.todocore.service.CategoryService
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
@RequestMapping("/api/v1/categories")
class CategoryController(
    private val categoryService: CategoryService,
) {
    @GetMapping
    fun readCategories(
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ApiResponse<List<CategoryResponse>> {
        val categoryInfo = categoryService.findAllByUserId(loginUser.id)
        val responseList = categoryInfo.map { CategoryResponse.of(it) }
        return ApiResponse.success(responseList)
    }

    @GetMapping("/{id}")
    fun read(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
    ): ApiResponse<CategoryResponse> {
        val categoryInfo = categoryService.findById(loginUser.id, id)
        return ApiResponse.success(CategoryResponse.of(categoryInfo))
    }

    @GetMapping("/search")
    fun getCategoryByName(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestParam name: String,
    ): ApiResponse<CategoryResponse> {
        val categoryInfo = categoryService.findByName(loginUser.id, name)
        return ApiResponse.success(CategoryResponse.of(categoryInfo))
    }

    @PostMapping
    fun addCategory(
        @AuthenticationPrincipal loginUser: LoginUser,
        @RequestBody command: AddCategoryRequest,
    ): ApiResponse<AddCategoryResponse> {
        val category = categoryService.add(loginUser.id, command.toAddCategory())
        return ApiResponse.success(AddCategoryResponse(category))
    }

    @PatchMapping("/{id}")
    fun modifyCategory(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
        @RequestBody command: ModifyCategoryRequest,
    ): ApiResponse<ModifyCategoryResponse> {
        val categoryId = categoryService.modify(loginUser.id, id, command.toModifyCategory())
        return ApiResponse.success(ModifyCategoryResponse(categoryId))
    }

    @DeleteMapping("/{id}")
    fun removeCategory(
        @AuthenticationPrincipal loginUser: LoginUser,
        @PathVariable id: Long,
    ): ApiResponse<DeleteCategoryResponse> {
        val removedId = categoryService.remove(loginUser.id, id)
        return ApiResponse.success(DeleteCategoryResponse(removedId))
    }
}
