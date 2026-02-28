package com.benecia.lifetracker.memocore.model.command

data class ModifyMemo(
    val title: String? = null,
    val content: String? = null,
    val categoryId: Long? = null,
    val categoryIdUpdated: Boolean = false,
    val tags: List<String>? = null,
)
