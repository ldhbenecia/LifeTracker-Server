package com.benecia.lifetracker.domain.ledger.dto

import com.benecia.lifetracker.ledgercore.model.command.AddLedgerCategory

data class AddLedgerCategoryRequest(
    val name: String,
    val icon: String,
    val color: String,
) {
    fun toCommand(): AddLedgerCategory = AddLedgerCategory(
        name = name,
        icon = icon,
        color = color,
    )
}
