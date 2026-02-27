package com.benecia.lifetracker.ledgercore.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LedgerEntryRepository {
    fun findByUserIdAndId(userId: UUID, id: Long): LedgerEntry?
    fun findAllByUserIdAndYearMonth(userId: UUID, year: Int, month: Int): List<LedgerEntry>
    fun add(entry: LedgerEntry): Long
    fun modify(id: Long, entry: LedgerEntry): Long?
    fun remove(id: Long): Long?
}
