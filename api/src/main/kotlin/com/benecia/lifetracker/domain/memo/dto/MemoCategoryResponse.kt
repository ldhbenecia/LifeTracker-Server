package com.benecia.lifetracker.domain.memo.dto

import com.benecia.lifetracker.memocore.model.info.MemoCategoryInfo

data class MemoCategoryResponse(
    val id: Long,
    val name: String,
    val icon: String,
    val color: String,
) {
    companion object {
        fun of(info: MemoCategoryInfo): MemoCategoryResponse = MemoCategoryResponse(id = info.id, name = info.name, icon = info.icon, color = info.color)
    }
}
