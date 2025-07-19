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
            id,
            user.provider,
            user.email,
            user.displayName,
            user.profileImageUrl,
        )
    }

    fun findByProviderAndEmail(provider: String, email: String): UserInfo {
        val user = userRepository.findByProviderAndEmail(provider, email)

        return UserInfo(
            user.id!!,
            user.provider,
            user.email,
            user.displayName,
            user.profileImageUrl,
        )
    }
}
