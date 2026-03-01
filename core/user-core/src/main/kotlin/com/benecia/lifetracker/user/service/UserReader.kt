package com.benecia.lifetracker.user.service

import com.benecia.lifetracker.user.model.info.UserInfo
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserReader(
    private val userRepository: UserRepository,
) {
    fun findById(id: UUID): UserInfo {
        val user = userRepository.findById(id)
        return UserInfo(
            id = id,
            provider = user.provider,
            email = user.email,
            displayName = user.displayName,
            profileImageUrl = user.profileImageUrl,
            userCode = user.userCode,
        )
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun findByProviderAndEmail(provider: String, email: String): User? {
        return userRepository.findByProviderAndEmail(provider, email)
    }

    fun findByUserCode(userCode: String): User? {
        return userRepository.findByUserCode(userCode)
    }
}
