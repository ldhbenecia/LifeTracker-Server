package com.benecia.lifetracker.memocore.model.command

data class AddMemo(
    val title: String,
    val content: String? = null,
    val categoryId: Long? = null,
    val tags: List<String> = emptyList(),
)
