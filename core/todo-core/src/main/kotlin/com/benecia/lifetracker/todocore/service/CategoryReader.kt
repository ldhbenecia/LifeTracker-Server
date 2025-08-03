package com.benecia.lifetracker.todocore.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.todocore.exception.CategoryErrorCode
import com.benecia.lifetracker.todocore.model.info.CategoryInfo
import org.springframework.stereotype.Component
import java.util.UUID

@Component
data class CategoryReader(
    private val categoryRepository: CategoryRepository,
) {
    fun findByUserIdAndId(userId: UUID, id: Long): CategoryInfo {
        val category = categoryRepository.findByUserIdAndId(userId, id)
            ?: throw CoreException(CategoryErrorCode.CATEGORY_NOT_FOUND)

        return CategoryInfo(
            id = id,
            name = category.name,
            icon = category.icon,
            color = category.color,
        )
    }

    fun findByUserIdAndName(userId: UUID, name: String): CategoryInfo {
        val category = categoryRepository.findByUserIdAndName(userId, name)
            ?: throw CoreException(CategoryErrorCode.CATEGORY_NOT_FOUND)

        return CategoryInfo(
            id = category.id!!,
            name = category.name,
            icon = category.icon,
            color = category.color,
        )
    }

    fun findByUserIdAndIds(userId: UUID, ids: List<Long>): List<CategoryInfo> {
        val categories = categoryRepository.findByUserIdAndIds(userId, ids)

        return categories.map { category ->
            CategoryInfo(
                id = category.id!!,
                name = category.name,
                icon = category.icon,
                color = category.color,
            )
        }
    }

    fun findAllByUserId(userId: UUID): List<CategoryInfo> {
        val categories = categoryRepository.findAllByUserId(userId)

        return categories.map { category ->
            CategoryInfo(
                id = category.id!!,
                name = category.name,
                icon = category.icon,
                color = category.color,
            )
        }
    }

    fun existsByUserIdAndId(userId: UUID, categoryId: Long): Boolean {
        return categoryRepository.existsByUserIdAndId(userId, categoryId)
    }
}
