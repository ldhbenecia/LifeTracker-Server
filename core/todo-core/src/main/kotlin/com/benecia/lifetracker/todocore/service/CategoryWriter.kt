package com.benecia.lifetracker.todocore.service

import com.benecia.lifetracker.todocore.model.command.AddCategory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
data class CategoryWriter(
    private val categoryReader: CategoryReader,
    private val categoryRepository: CategoryRepository,
) {
    fun add(userId: UUID, command: AddCategory): Long {
        val category = Category(
            userId = userId,
            name = command.name,
            icon = command.icon,
            color = command.color,
        )

        return categoryRepository.add(category)
    }
}
