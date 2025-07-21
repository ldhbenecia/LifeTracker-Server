package com.benecia.lifetracker.domain.todo.dto

import com.benecia.lifetracker.todocore.model.command.ModifyCategory

data class ModifyCategoryRequest(
    val name: String? = null,
    val icon: String? = null,
    val color: String? = null,
) {
    fun toModifyCategory(): ModifyCategory {
        return ModifyCategory(
            name = this.name,
            icon = this.icon,
            color = this.color,
        )
    }
}
