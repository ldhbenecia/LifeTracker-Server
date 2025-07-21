package com.benecia.lifetracker.todocore.model.command

data class ModifyCategory(
    val name: String? = null,
    val icon: String? = null,
    val color: String? = null,
)
