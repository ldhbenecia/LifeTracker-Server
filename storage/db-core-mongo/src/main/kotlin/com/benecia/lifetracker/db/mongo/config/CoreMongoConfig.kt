package com.benecia.lifetracker.db.mongo.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.benecia.lifetracker.db.mongo"])
@EnableMongoAuditing
internal class CoreMongoConfig
