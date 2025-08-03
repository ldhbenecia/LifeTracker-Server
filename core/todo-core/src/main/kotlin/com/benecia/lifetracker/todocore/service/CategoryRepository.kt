package com.benecia.lifetracker.todocore.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CategoryRepository {
    fun findByUserIdAndId(userId: UUID, id: Long): Category
    fun findByUserIdAndIds(userId: UUID, ids: List<Long>): List<Category>
    fun findByUserIdAndName(userId: UUID, name: String): Category
    fun findAllByUserId(userId: UUID): List<Category>
    fun existsByUserIdAndId(userId: UUID, categoryId: Long): Boolean
    fun add(category: Category): Long
    fun modify(id: Long, category: Category): Long
    fun remove(id: Long): Long
}
