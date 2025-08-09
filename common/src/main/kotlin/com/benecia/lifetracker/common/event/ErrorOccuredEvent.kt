package com.benecia.lifetracker.common.event

data class ErrorOccuredEvent(
    val exception: Throwable,
)
