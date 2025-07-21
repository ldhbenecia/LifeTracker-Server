package com.benecia.lifetracker.todocore.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.todocore.exception.CategoryErrorCode
import com.benecia.lifetracker.todocore.model.command.AddCategory
import com.benecia.lifetracker.todocore.model.command.ModifyCategory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
data class CategoryWriter(
    private val categoryReader: CategoryReader,
    private val categoryRepository: CategoryRepository,
) {
    fun add(userId: UUID, command: AddCategory): Long {
        if (categoryReader.existsByUserIdAndName(userId, command.name)) {
            throw CoreException(CategoryErrorCode.DUPLICATE_CATEGORY_NAME)
        }

        val category = Category(
            userId = userId,
            name = command.name,
            icon = command.icon,
            color = command.color,
        )

        return categoryRepository.add(category)
    }

    fun modify(userId: UUID, id: Long, command: ModifyCategory): Long {
        val existing = categoryReader.findByUserIdAndId(userId, id)

        // name이 변경되었고, 변경된 이름이 null이 아니면 중복 검사
        if (command.name != null && existing.name != command.name) {
            if (categoryReader.existsByUserIdAndName(userId, command.name)) {
                throw CoreException(CategoryErrorCode.DUPLICATE_CATEGORY_NAME)
            }
        }

        val updatedCategory = Category(
            id = existing.id,
            userId = userId,
            name = command.name ?: existing.name,
            icon = command.icon ?: existing.icon,
            color = command.color ?: existing.color,
        )

        return categoryRepository.modify(id, updatedCategory)
    }
}
