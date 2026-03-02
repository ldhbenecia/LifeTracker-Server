package com.benecia.lifetracker.todocore.model.info

data class TodoStatisticsInfo(
    val year: Int,
    val month: Int,
    val totalCount: Int,
    val doneCount: Int,
    val pendingCount: Int,
    val categoryBreakdown: List<CategoryStatisticsInfo>,
)

data class CategoryStatisticsInfo(
    val categoryId: Long?,
    val categoryName: String?,
    val totalCount: Int,
    val doneCount: Int,
)
