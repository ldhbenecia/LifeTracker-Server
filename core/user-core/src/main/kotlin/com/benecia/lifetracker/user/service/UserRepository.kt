package com.benecia.lifetracker.user.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository {
    fun add(user: User): UUID
    fun update(user: User): User
    fun findById(id: UUID): User
    fun findByProviderAndEmail(provider: String, email: String): User?
}
