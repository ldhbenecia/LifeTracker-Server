package com.benecia.lifetracker.db.core.user

import com.benecia.lifetracker.user.service.Friend
import com.benecia.lifetracker.user.service.FriendRepository
import com.benecia.lifetracker.user.service.FriendStatus
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class FriendEntityRepository(
    private val friendJpaRepository: FriendJpaRepository,
) : FriendRepository {

    override fun add(requesterId: UUID, receiverId: UUID): Long {
        val entity = FriendEntity(
            requesterId = requesterId,
            receiverId = receiverId,
        )

        return friendJpaRepository.save(entity).id!!
    }

    override fun exists(requesterId: UUID, receiverId: UUID): Boolean {
        return friendJpaRepository.existsByRequesterIdAndReceiverId(requesterId, receiverId)
    }

    override fun findAllByUserId(userId: UUID): List<Friend> {
        return friendJpaRepository.findAllByRequesterIdOrReceiverId(userId, userId)
            .filter { it.status == FriendStatus.ACCEPTED }
            .map { it.toDomain() }
    }
}
