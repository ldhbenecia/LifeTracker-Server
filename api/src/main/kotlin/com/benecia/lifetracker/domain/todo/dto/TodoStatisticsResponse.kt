package com.benecia.lifetracker.domain.todo.dto

import com.benecia.lifetracker.todocore.model.info.CategoryStatisticsInfo
import com.benecia.lifetracker.todocore.model.info.TodoStatisticsInfo

data class TodoStatisticsResponse(
    val year: Int,
    val month: Int,
    val totalCount: Int,
    val doneCount: Int,
    val pendingCount: Int,
    val categoryBreakdown: List<CategoryStatisticsResponse>,
) {
    companion object {
        fun of(info: TodoStatisticsInfo) = TodoStatisticsResponse(
            year = info.year,
            month = info.month,
            totalCount = info.totalCount,
            doneCount = info.doneCount,
            pendingCount = info.pendingCount,
            categoryBreakdown = info.categoryBreakdown.map { CategoryStatisticsResponse.of(it) },
        )
    }
}

data class CategoryStatisticsResponse(
    val categoryId: Long?,
    val categoryName: String?,
    val totalCount: Int,
    val doneCount: Int,
) {
    companion object {
        fun of(info: CategoryStatisticsInfo) = CategoryStatisticsResponse(
            categoryId = info.categoryId,
            categoryName = info.categoryName,
            totalCount = info.totalCount,
            doneCount = info.doneCount,
        )
    }
}
