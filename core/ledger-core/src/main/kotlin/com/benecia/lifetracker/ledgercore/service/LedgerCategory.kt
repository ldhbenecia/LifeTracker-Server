package com.benecia.lifetracker.ledgercore.service

import java.util.UUID

data class LedgerCategory(
    val id: Long? = null,
    val userId: UUID,
    val name: String,
    val icon: String,
    val color: String,
)
