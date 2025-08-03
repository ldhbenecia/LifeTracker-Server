package com.benecia.lifetracker.domain.todo.dto

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.todocore.exception.CategoryErrorCode
import com.benecia.lifetracker.todocore.model.command.AddCategory

data class AddCategoryRequest(
    val name: String,
    val icon: String,
    val color: String,
) {
    fun toAddCategory(): AddCategory {
        if (name.length > 10) {
            throw CoreException(CategoryErrorCode.CATEGORY_NAME_TOO_LONG)
        }

        return AddCategory(
            name = name,
            icon = icon,
            color = color,
        )
    }
}
