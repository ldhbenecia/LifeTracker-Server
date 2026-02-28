package com.benecia.lifetracker.domain.memo.dto

import com.benecia.lifetracker.memocore.model.command.AddMemoCategory

data class AddMemoCategoryRequest(
    val name: String,
    val icon: String,
    val color: String,
) {
    fun toCommand(): AddMemoCategory = AddMemoCategory(name = name, icon = icon, color = color)
}
