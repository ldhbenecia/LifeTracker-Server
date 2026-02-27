package com.benecia.lifetracker.domain.ledger.dto

import com.benecia.lifetracker.ledgercore.model.info.LedgerCategoryInfo

data class LedgerCategoryResponse(
    val id: Long,
    val name: String,
    val icon: String,
    val color: String,
) {
    companion object {
        fun of(info: LedgerCategoryInfo): LedgerCategoryResponse = LedgerCategoryResponse(
            id = info.id,
            name = info.name,
            icon = info.icon,
            color = info.color,
        )
    }
}
