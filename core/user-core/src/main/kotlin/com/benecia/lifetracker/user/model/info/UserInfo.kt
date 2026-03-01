package com.benecia.lifetracker.user.model.info

import com.benecia.lifetracker.user.service.User
import java.util.UUID

data class UserInfo(
    val id: UUID,
    val provider: String,
    val email: String,
    val displayName: String,
    val profileImageUrl: String?,
    val userCode: String,
) {
    fun toUser(): User = User(
        id = this.id,
        provider = this.provider,
        email = this.email,
        displayName = this.displayName,
        profileImageUrl = this.profileImageUrl,
        userCode = this.userCode,
    )
}
