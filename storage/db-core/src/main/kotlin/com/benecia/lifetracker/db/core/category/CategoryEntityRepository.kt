package com.benecia.lifetracker.db.core.category

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.todocore.exception.CategoryErrorCode
import com.benecia.lifetracker.todocore.service.Category
import com.benecia.lifetracker.todocore.service.CategoryRepository
import org.springframework.data.repository.findByIdOrNull
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

    override fun findByUserIdAndIds(userId: UUID, ids: List<Long>): List<Category> {
        if (ids.isEmpty()) return emptyList()
        return categoryJpaRepository.findByUserIdAndIdIn(userId, ids).map { it.toDomain() }
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

    override fun existsByUserIdAndName(userId: UUID, name: String): Boolean {
        return categoryJpaRepository.existsByUserIdAndName(userId, name)
    }

    override fun add(category: Category): Long {
        val entity = CategoryEntity.from(category)
        return categoryJpaRepository.save(entity).id
            ?: throw CoreException(CategoryErrorCode.CATEGORY_PERSIST_FAILED)
    }

    override fun modify(id: Long, category: Category): Long {
        val entity = categoryJpaRepository.findByIdOrNull(id)
            ?: throw CoreException(CategoryErrorCode.CATEGORY_NOT_FOUND)

        entity.name = category.name
        entity.icon = category.icon
        entity.color = category.color

        return categoryJpaRepository.save(entity).id
            ?: throw CoreException(CategoryErrorCode.CATEGORY_PERSIST_FAILED)
    }
}
