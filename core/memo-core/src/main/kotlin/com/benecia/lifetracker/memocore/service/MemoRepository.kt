package com.benecia.lifetracker.memocore.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MemoRepository {
    fun findByUserIdAndId(userId: UUID, id: Long): Memo?
    fun findAllByUserId(userId: UUID): List<Memo>
    fun add(memo: Memo): Long
    fun modify(id: Long, memo: Memo): Long?
    fun remove(id: Long): Long?
}
