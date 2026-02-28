package com.benecia.lifetracker.domain.memo.dto

import com.benecia.lifetracker.memocore.model.command.ModifyMemoCategory

data class ModifyMemoCategoryRequest(
    val name: String? = null,
    val icon: String? = null,
    val color: String? = null,
) {
    fun toCommand(): ModifyMemoCategory = ModifyMemoCategory(name = name, icon = icon, color = color)
}
