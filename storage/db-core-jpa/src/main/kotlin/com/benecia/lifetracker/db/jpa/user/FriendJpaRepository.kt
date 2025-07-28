package com.benecia.lifetracker.db.jpa.user

import com.benecia.lifetracker.user.service.FriendStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FriendJpaRepository : JpaRepository<com.benecia.lifetracker.db.jpa.user.FriendEntity, Long> {
    fun existsByRequesterIdAndReceiverId(requesterId: UUID, receiverId: UUID): Boolean

    fun findAllByRequesterIdOrReceiverId(requesterId: UUID, receiverId: UUID): List<com.benecia.lifetracker.db.jpa.user.FriendEntity>

    fun findAllByReceiverIdAndStatus(receiverId: UUID, status: FriendStatus): List<com.benecia.lifetracker.db.jpa.user.FriendEntity>
}
