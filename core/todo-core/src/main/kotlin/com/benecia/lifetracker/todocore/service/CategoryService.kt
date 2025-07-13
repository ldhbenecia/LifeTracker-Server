package com.benecia.lifetracker.todocore.service

import com.benecia.lifetracker.todocore.model.command.AddCategory
import com.benecia.lifetracker.todocore.model.info.CategoryInfo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CategoryService(
    private val categoryReader: CategoryReader,
    private val categoryWriter: CategoryWriter,
) {
    fun findById(userId: UUID, id: Long): CategoryInfo {
        return categoryReader.findByUserIdAndId(userId, id)
    }

    fun findByName(userId: UUID, name: String): CategoryInfo {
        return categoryReader.findByUserIdAndName(userId, name)
    }

    fun findAllByUserId(userId: UUID): List<CategoryInfo> {
        return categoryReader.findAllByUserId(userId)
    }

    fun add(userId: UUID, command: AddCategory): Long {
        return categoryWriter.add(userId, command)
    }
}
