package com.benecia.lifetracker.memocore.service

import java.util.UUID

data class MemoCategory(
    val id: Long? = null,
    val userId: UUID,
    val name: String,
    val icon: String,
    val color: String,
)
