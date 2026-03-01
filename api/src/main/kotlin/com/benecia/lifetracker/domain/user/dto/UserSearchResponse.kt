package com.benecia.lifetracker.domain.user.dto

import com.benecia.lifetracker.user.service.User

data class UserSearchResponse(
    val userCode: String,
    val displayName: String,
    val profileImageUrl: String?,
) {
    companion object {
        fun of(user: User): UserSearchResponse = UserSearchResponse(
            userCode = user.userCode,
            displayName = user.displayName,
            profileImageUrl = user.profileImageUrl,
        )
    }
}
