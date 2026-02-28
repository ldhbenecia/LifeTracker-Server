package com.benecia.lifetracker.memocore.model.command

data class ModifyMemoCategory(
    val name: String? = null,
    val icon: String? = null,
    val color: String? = null,
)
