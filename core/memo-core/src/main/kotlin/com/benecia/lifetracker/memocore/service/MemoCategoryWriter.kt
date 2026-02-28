package com.benecia.lifetracker.memocore.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.memocore.exception.MemoErrorCode
import com.benecia.lifetracker.memocore.model.command.AddMemoCategory
import com.benecia.lifetracker.memocore.model.command.ModifyMemoCategory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MemoCategoryWriter(
    private val memoCategoryReader: MemoCategoryReader,
    private val memoCategoryRepository: MemoCategoryRepository,
) {
    fun add(
        userId: UUID,
        command: AddMemoCategory,
    ): Long {
        val category = MemoCategory(userId = userId, name = command.name, icon = command.icon, color = command.color)
        return memoCategoryRepository.add(category)
    }

    fun modify(
        userId: UUID,
        id: Long,
        command: ModifyMemoCategory,
    ): Long {
        val existing = memoCategoryReader.findByUserIdAndId(userId, id)
        val updated = MemoCategory(
            id = id,
            userId = userId,
            name = command.name ?: existing.name,
            icon = command.icon ?: existing.icon,
            color = command.color ?: existing.color,
        )
        return memoCategoryRepository.modify(id, updated)
            ?: throw CoreException(MemoErrorCode.MEMO_CATEGORY_NOT_FOUND)
    }

    fun remove(
        userId: UUID,
        id: Long,
    ): Long {
        memoCategoryReader.findByUserIdAndId(userId, id)
        return memoCategoryRepository.remove(id)
            ?: throw CoreException(MemoErrorCode.MEMO_CATEGORY_NOT_FOUND)
    }
}
