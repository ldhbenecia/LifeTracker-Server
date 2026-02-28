package com.benecia.lifetracker.memocore.service

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.memocore.exception.MemoErrorCode
import com.benecia.lifetracker.memocore.model.info.MemoInfo
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MemoReader(
    private val memoRepository: MemoRepository,
    private val memoCategoryReader: MemoCategoryReader,
) {
    fun findByUserIdAndId(
        userId: UUID,
        id: Long,
    ): MemoInfo {
        val memo = memoRepository.findByUserIdAndId(userId, id)
            ?: throw CoreException(MemoErrorCode.MEMO_NOT_FOUND)
        val category = memo.categoryId?.let {
            runCatching { memoCategoryReader.findByUserIdAndId(userId, it) }.getOrNull()
        }
        return MemoInfo(id = memo.id!!, title = memo.title, content = memo.content, category = category, tags = memo.tags)
    }

    fun findAllByUserId(userId: UUID): List<MemoInfo> {
        val memos = memoRepository.findAllByUserId(userId)
        val categoryIds = memos.mapNotNull { it.categoryId }.distinct()
        val categoryMap = categoryIds
            .mapNotNull { id ->
                runCatching { memoCategoryReader.findByUserIdAndId(userId, id) }.getOrNull()?.let { id to it }
            }
            .toMap()
        return memos.map { memo ->
            MemoInfo(
                id = memo.id!!,
                title = memo.title,
                content = memo.content,
                category = memo.categoryId?.let { categoryMap[it] },
                tags = memo.tags,
            )
        }
    }
}
