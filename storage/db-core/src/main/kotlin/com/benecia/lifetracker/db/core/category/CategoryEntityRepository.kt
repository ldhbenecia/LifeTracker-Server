package com.benecia.lifetracker.db.core.category

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.todocore.exception.CategoryErrorCode
import com.benecia.lifetracker.todocore.service.Category
import com.benecia.lifetracker.todocore.service.CategoryRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class CategoryEntityRepository(
    private val categoryJpaRepository: CategoryJpaRepository,
) : CategoryRepository {

    override fun findByUserIdAndId(userId: UUID, id: Long): Category {
        val entity = categoryJpaRepository.findByUserIdAndId(userId, id)
            ?: throw CoreException(CategoryErrorCode.CATEGORY_NOT_FOUND)
        return entity.toDomain()
    }

    override fun findByUserIdAndName(userId: UUID, name: String): Category {
        val entity = categoryJpaRepository.findByUserIdAndName(userId, name)
            ?: throw CoreException(CategoryErrorCode.CATEGORY_NOT_FOUND)
        return entity.toDomain()
    }

    override fun findAllByUserId(userId: UUID): List<Category> {
        val entities = categoryJpaRepository.findAllByUserId(userId)
        return entities.map { it.toDomain() }
    }

    override fun add(category: Category): Long {
        val entity = CategoryEntity.from(category)
        return categoryJpaRepository.save(entity).id!!
    }
}
