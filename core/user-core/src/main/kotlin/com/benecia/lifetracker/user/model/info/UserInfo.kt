package com.benecia.lifetracker.user.model.info

import java.util.UUID

data class UserInfo(
    val id: UUID,
    val provider: String,
    val email: String,
    val displayName: String,
    val profileImageUrl: String?,
)
