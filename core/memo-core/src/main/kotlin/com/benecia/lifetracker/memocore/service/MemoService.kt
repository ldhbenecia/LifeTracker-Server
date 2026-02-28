package com.benecia.lifetracker.memocore.service

import com.benecia.lifetracker.memocore.model.command.AddMemo
import com.benecia.lifetracker.memocore.model.command.ModifyMemo
import com.benecia.lifetracker.memocore.model.info.MemoInfo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemoService(
    private val memoReader: MemoReader,
    private val memoWriter: MemoWriter,
) {
    fun findAll(userId: UUID): List<MemoInfo> = memoReader.findAllByUserId(userId)

    fun add(
        userId: UUID,
        command: AddMemo,
    ): Long = memoWriter.add(userId, command)

    fun modify(
        userId: UUID,
        id: Long,
        command: ModifyMemo,
    ): Long = memoWriter.modify(userId, id, command)

    fun remove(
        userId: UUID,
        id: Long,
    ): Long = memoWriter.remove(userId, id)
}
