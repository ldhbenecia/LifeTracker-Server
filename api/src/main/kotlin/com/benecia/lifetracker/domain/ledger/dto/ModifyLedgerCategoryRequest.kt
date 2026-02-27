package com.benecia.lifetracker.domain.ledger.dto

import com.benecia.lifetracker.ledgercore.model.command.ModifyLedgerCategory

data class ModifyLedgerCategoryRequest(
    val name: String?,
    val icon: String?,
    val color: String?,
) {
    fun toCommand(): ModifyLedgerCategory = ModifyLedgerCategory(
        name = name,
        icon = icon,
        color = color,
    )
}
