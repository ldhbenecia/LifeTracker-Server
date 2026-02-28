package com.benecia.lifetracker.memocore.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MemoCategoryRepository {
    fun findByUserIdAndId(userId: UUID, id: Long): MemoCategory?
    fun findAllByUserId(userId: UUID): List<MemoCategory>
    fun add(category: MemoCategory): Long
    fun modify(id: Long, category: MemoCategory): Long?
    fun remove(id: Long): Long?
}
