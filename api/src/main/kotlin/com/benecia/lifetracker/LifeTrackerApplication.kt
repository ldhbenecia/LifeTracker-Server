package com.benecia.lifetracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class LifeTrackerApplication

fun main(args: Array<String>) {
    runApplication<LifeTrackerApplication>(*args)
}
