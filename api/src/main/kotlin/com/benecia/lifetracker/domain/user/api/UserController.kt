package com.benecia.lifetracker.domain.user.api

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.domain.user.dto.UserResponse
import com.benecia.lifetracker.domain.user.dto.UserSearchResponse
import com.benecia.lifetracker.user.exception.UserErrorCode
import com.benecia.lifetracker.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/{id}")
    fun findUserById(
        @PathVariable id: UUID,
    ): ApiResponse<UserResponse> {
        val user = userService.findById(id)
        return ApiResponse.success(UserResponse.of(user))
    }

    @GetMapping("/search")
    fun searchByUserCode(
        @RequestParam code: String,
    ): ApiResponse<UserSearchResponse> {
        val user = userService.findByUserCode(code)
            ?: throw CoreException(UserErrorCode.USER_NOT_FOUND)
        return ApiResponse.success(UserSearchResponse.of(user))
    }
}
