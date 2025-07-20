package com.benecia.lifetracker.db.core.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FriendJpaRepository : JpaRepository<FriendEntity, Long> {
    fun existsByRequesterIdAndReceiverId(requesterId: UUID, receiverId: UUID): Boolean

    fun findAllByRequesterIdOrReceiverId(requesterId: UUID, receiverId: UUID): List<FriendEntity>
}
