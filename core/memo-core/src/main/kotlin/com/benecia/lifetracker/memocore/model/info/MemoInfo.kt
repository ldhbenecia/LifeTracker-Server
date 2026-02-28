package com.benecia.lifetracker.memocore.model.info

data class MemoInfo(
    val id: Long,
    val title: String,
    val content: String?,
    val category: MemoCategoryInfo?,
    val tags: List<String>,
)
