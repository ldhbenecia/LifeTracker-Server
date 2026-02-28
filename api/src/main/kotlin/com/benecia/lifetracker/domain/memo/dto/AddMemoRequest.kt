package com.benecia.lifetracker.domain.memo.dto

import com.benecia.lifetracker.memocore.model.command.AddMemo

data class AddMemoRequest(
    val title: String,
    val content: String? = null,
    val categoryId: Long? = null,
    val tags: List<String> = emptyList(),
) {
    fun toCommand(): AddMemo = AddMemo(title = title, content = content, categoryId = categoryId, tags = tags)
}
