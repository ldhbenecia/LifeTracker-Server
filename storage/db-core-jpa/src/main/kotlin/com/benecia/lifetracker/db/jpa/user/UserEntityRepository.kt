package com.benecia.lifetracker.db.jpa.user

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.user.exception.UserErrorCode
import com.benecia.lifetracker.user.service.User
import com.benecia.lifetracker.user.service.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserEntityRepository(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {

    override fun add(user: User): UUID {
        val entity = UserEntity(
            provider = user.provider,
            email = user.email,
            displayName = user.displayName,
            profileImageUrl = user.profileImageUrl,
            userCode = generateUniqueUserCode(),
        )
        return userJpaRepository.save(entity).id!!
    }

    override fun findById(id: UUID): User {
        val entity = userJpaRepository.findByIdOrNull(id)
            ?: throw CoreException(UserErrorCode.USER_NOT_FOUND)
        return entity.toDomain()
    }

    override fun findByEmail(email: String): User? {
        return userJpaRepository.findByEmail(email)?.toDomain()
    }

    override fun findByProviderAndEmail(provider: String, email: String): User? {
        return userJpaRepository.findByProviderAndEmail(provider, email)?.toDomain()
    }

    override fun findByUserCode(userCode: String): User? {
        return userJpaRepository.findByUserCode(userCode)?.toDomain()
    }

    override fun update(user: User): User {
        val entity = userJpaRepository.findByIdOrNull(user.id!!)
            ?: throw CoreException(UserErrorCode.USER_NOT_FOUND)
        entity.displayName = user.displayName
        entity.profileImageUrl = user.profileImageUrl
        return entity.toDomain()
    }

    private fun generateUniqueUserCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        repeat(10) {
            val code = (1..8).map { chars.random() }.joinToString("")
            if (!userJpaRepository.existsByUserCode(code)) return code
        }
        throw CoreException(UserErrorCode.USER_CODE_GENERATION_FAILED)
    }
}
