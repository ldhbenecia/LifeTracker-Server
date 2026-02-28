package com.benecia.lifetracker.memocore.service

import com.benecia.lifetracker.memocore.model.command.AddMemoCategory
import com.benecia.lifetracker.memocore.model.command.ModifyMemoCategory
import com.benecia.lifetracker.memocore.model.info.MemoCategoryInfo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemoCategoryService(
    private val memoCategoryReader: MemoCategoryReader,
    private val memoCategoryWriter: MemoCategoryWriter,
) {
    fun findAll(userId: UUID): List<MemoCategoryInfo> = memoCategoryReader.findAllByUserId(userId)

    fun add(
        userId: UUID,
        command: AddMemoCategory,
    ): Long = memoCategoryWriter.add(userId, command)

    fun modify(
        userId: UUID,
        id: Long,
        command: ModifyMemoCategory,
    ): Long = memoCategoryWriter.modify(userId, id, command)

    fun remove(
        userId: UUID,
        id: Long,
    ): Long = memoCategoryWriter.remove(userId, id)
}
