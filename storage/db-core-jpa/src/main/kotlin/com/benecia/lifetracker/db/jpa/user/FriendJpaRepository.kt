package com.benecia.lifetracker.db.jpa.user

import com.benecia.lifetracker.user.service.FriendStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FriendJpaRepository : JpaRepository<FriendEntity, Long> {
    fun existsByRequesterIdAndReceiverId(requesterId: UUID, receiverId: UUID): Boolean

    fun findAllByRequesterIdOrReceiverId(requesterId: UUID, receiverId: UUID): List<FriendEntity>

    fun findAllByReceiverIdAndStatus(receiverId: UUID, status: FriendStatus): List<FriendEntity>

    fun findAllByRequesterIdAndStatus(requesterId: UUID, status: FriendStatus): List<FriendEntity>
}
