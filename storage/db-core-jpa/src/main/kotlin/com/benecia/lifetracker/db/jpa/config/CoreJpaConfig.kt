package com.benecia.lifetracker.db.jpa.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = ["com.benecia.lifetracker.db.jpa"])
@EnableJpaRepositories(basePackages = ["com.benecia.lifetracker.db.jpa"])
internal class CoreJpaConfig
