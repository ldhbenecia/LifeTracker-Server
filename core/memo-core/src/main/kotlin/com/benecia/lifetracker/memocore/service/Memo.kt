package com.benecia.lifetracker.memocore.service

import java.util.UUID

data class Memo(
    val id: Long? = null,
    val userId: UUID,
    val categoryId: Long?,
    val title: String,
    val content: String?,
    val tags: List<String>,
    val status: MemoStatus = MemoStatus.ACTIVE,
)
