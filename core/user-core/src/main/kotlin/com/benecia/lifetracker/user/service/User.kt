package com.benecia.lifetracker.user.service

import java.util.UUID

data class User(
    val id: UUID? = null,
    val provider: String,
    val email: String,
    val displayName: String,
    val profileImageUrl: String?,
    val userCode: String = "",
)
