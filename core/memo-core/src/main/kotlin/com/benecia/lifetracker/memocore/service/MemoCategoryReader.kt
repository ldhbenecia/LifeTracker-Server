package com.benecia.lifetracker.memocore.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.memocore.exception.MemoErrorCode
import com.benecia.lifetracker.memocore.model.info.MemoCategoryInfo
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MemoCategoryReader(
    private val memoCategoryRepository: MemoCategoryRepository,
) {
    fun findByUserIdAndId(
        userId: UUID,
        id: Long,
    ): MemoCategoryInfo {
        val category = memoCategoryRepository.findByUserIdAndId(userId, id)
            ?: throw CoreException(MemoErrorCode.MEMO_CATEGORY_NOT_FOUND)
        return MemoCategoryInfo(id = category.id!!, name = category.name, icon = category.icon, color = category.color)
    }

    fun findAllByUserId(userId: UUID): List<MemoCategoryInfo> {
        return memoCategoryRepository.findAllByUserId(userId)
            .map { MemoCategoryInfo(id = it.id!!, name = it.name, icon = it.icon, color = it.color) }
    }

    fun existsByUserIdAndId(
        userId: UUID,
        id: Long,
    ): Boolean = memoCategoryRepository.findByUserIdAndId(userId, id) != null
}
