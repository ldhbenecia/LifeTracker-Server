package com.benecia.lifetracker.domain.user.dto

import com.benecia.lifetracker.user.model.info.UserInfo
import java.util.UUID

data class UserResponse(
    val id: UUID? = null,
    val provider: String,
    val email: String,
    val displayName: String,
    val profileImageUrl: String?,
) {
    companion object {
        fun of(info: UserInfo) = UserResponse(
            id = info.id,
            provider = info.provider,
            email = info.email,
            displayName = info.displayName,
            profileImageUrl = info.profileImageUrl,
        )
    }
}
