package com.benecia.lifetracker.db.jpa.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserJpaRepository : JpaRepository<UserEntity, UUID> {
    fun findByProviderAndEmail(provider: String, email: String): UserEntity?
    fun findByEmail(email: String): UserEntity?
    fun findByUserCode(userCode: String): UserEntity?
}
