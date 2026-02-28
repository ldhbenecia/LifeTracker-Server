package com.benecia.lifetracker.domain.memo.dto

import com.benecia.lifetracker.memocore.model.command.ModifyMemo

data class ModifyMemoRequest(
    val title: String? = null,
    val content: String? = null,
    val categoryId: Long? = null,
    val categoryIdUpdated: Boolean = false,
    val tags: List<String>? = null,
) {
    fun toCommand(): ModifyMemo = ModifyMemo(
        title = title,
        content = content,
        categoryId = categoryId,
        categoryIdUpdated = categoryIdUpdated,
        tags = tags,
    )
}
