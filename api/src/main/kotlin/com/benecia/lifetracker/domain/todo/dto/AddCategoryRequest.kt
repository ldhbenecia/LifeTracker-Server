package com.benecia.lifetracker.domain.todo.dto

import com.benecia.lifetracker.todocore.model.command.AddCategory

data class AddCategoryRequest(
    val name: String,
    val icon: String,
    val color: String,
) {
    fun toAddCategory(): AddCategory {
        return AddCategory(
            name = name,
            icon = icon,
            color = color,
        )
    }
}
