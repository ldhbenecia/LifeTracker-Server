package com.benecia.lifetracker.todocore.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CategoryRepository {
    fun findByUserIdAndId(userId: UUID, id: Long): Category
    fun findByUserIdAndName(userId: UUID, name: String): Category
    fun findAllByUserId(userId: UUID): List<Category>
    fun add(category: Category): Long
}
