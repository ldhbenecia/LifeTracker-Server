package com.benecia.lifetracker.user.service

import com.benecia.lifetracker.user.model.info.UserInfo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userWriter: UserWriter,
    private val userReader: UserReader,
) {
    fun add(user: User): User {
        val userId = userWriter.add(user)
        return user.copy(id = userId)
    }

    fun update(user: User): User {
        return userWriter.update(user)
    }

    fun findById(id: UUID): UserInfo {
        return userReader.findById(id)
    }

    fun findByProviderAndEmail(provider: String, email: String): UserInfo {
        return userReader.findByProviderAndEmail(provider, email)
    }
}
