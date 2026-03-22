package com.benecia.lifetracker.db.jpa.fcm

import com.benecia.lifetracker.user.service.FcmToken
import com.benecia.lifetracker.user.service.FcmTokenRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class FcmTokenEntityRepository(
    private val fcmTokenJpaRepository: FcmTokenJpaRepository,
) : FcmTokenRepository {

    override fun save(fcmToken: FcmToken): Long {
        val existing = fcmTokenJpaRepository.findByUserIdAndToken(fcmToken.userId, fcmToken.token)
        return existing?.id ?: fcmTokenJpaRepository.save(FcmTokenEntity.from(fcmToken)).id!!
    }

    override fun delete(userId: UUID, token: String) {
        fcmTokenJpaRepository.deleteByUserIdAndToken(userId, token)
    }

    override fun findAllByUserId(userId: UUID): List<FcmToken> {
        return fcmTokenJpaRepository.findAllByUserId(userId).map { it.toDomain() }
    }
}
