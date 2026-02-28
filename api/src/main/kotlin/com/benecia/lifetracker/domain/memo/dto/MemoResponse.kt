package com.benecia.lifetracker.domain.memo.dto

import com.benecia.lifetracker.memocore.model.info.MemoInfo

data class MemoResponse(
    val id: Long,
    val title: String,
    val content: String?,
    val category: MemoCategoryResponse?,
    val tags: List<String>,
) {
    companion object {
        fun of(info: MemoInfo): MemoResponse = MemoResponse(
            id = info.id,
            title = info.title,
            content = info.content,
            category = info.category?.let { MemoCategoryResponse.of(it) },
            tags = info.tags,
        )
    }
}
