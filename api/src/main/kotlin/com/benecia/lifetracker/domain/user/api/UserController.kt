package com.benecia.lifetracker.domain.user.api

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.common.response.ApiResponse
import com.benecia.lifetracker.domain.user.dto.UserResponse
import com.benecia.lifetracker.user.exception.UserErrorCode
import com.benecia.lifetracker.user.model.info.UserInfo
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
    fun findUserByProviderAndEmail(
        @RequestParam provider: String,
        @RequestParam email: String,
    ): ApiResponse<UserResponse> {
        val user = userService.findByProviderAndEmail(provider, email)
        val userInfo = user?.let {
            UserInfo(
                id = it.id!!,
                provider = it.provider,
                email = it.email,
                displayName = it.displayName,
                profileImageUrl = it.profileImageUrl,
            )
        } ?: throw CoreException(UserErrorCode.USER_NOT_FOUND)
        return ApiResponse.success(UserResponse.of(userInfo))
    }
}
