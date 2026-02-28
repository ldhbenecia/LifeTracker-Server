package com.benecia.lifetracker.memocore.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.memocore.exception.MemoErrorCode
import com.benecia.lifetracker.memocore.model.command.AddMemo
import com.benecia.lifetracker.memocore.model.command.ModifyMemo
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MemoWriter(
    private val memoReader: MemoReader,
    private val memoCategoryReader: MemoCategoryReader,
    private val memoRepository: MemoRepository,
) {
    fun add(
        userId: UUID,
        command: AddMemo,
    ): Long {
        val categoryId = command.categoryId?.also {
            if (!memoCategoryReader.existsByUserIdAndId(userId, it)) {
                throw CoreException(MemoErrorCode.MEMO_CATEGORY_NOT_FOUND)
            }
        }
        val memo = Memo(
            userId = userId,
            categoryId = categoryId,
            title = command.title,
            content = command.content,
            tags = command.tags,
        )
        return memoRepository.add(memo)
    }

    fun modify(
        userId: UUID,
        id: Long,
        command: ModifyMemo,
    ): Long {
        val existing = memoReader.findByUserIdAndId(userId, id)
        val newCategoryId = when {
            command.categoryIdUpdated -> command.categoryId?.also {
                if (!memoCategoryReader.existsByUserIdAndId(userId, it)) {
                    throw CoreException(MemoErrorCode.MEMO_CATEGORY_NOT_FOUND)
                }
            }
            else -> existing.category?.id
        }
        val updated = Memo(
            id = id,
            userId = userId,
            categoryId = newCategoryId,
            title = command.title ?: existing.title,
            content = command.content ?: existing.content,
            tags = command.tags ?: existing.tags,
        )
        return memoRepository.modify(id, updated)
            ?: throw CoreException(MemoErrorCode.MEMO_NOT_FOUND)
    }

    fun remove(
        userId: UUID,
        id: Long,
    ): Long {
        memoReader.findByUserIdAndId(userId, id)
        return memoRepository.remove(id)
            ?: throw CoreException(MemoErrorCode.MEMO_NOT_FOUND)
    }
}
