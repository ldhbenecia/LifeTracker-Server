package com.benecia.lifetracker.db.jpa.user

import com.benecia.lifetracker.common.exception.CoreException
import com.benecia.lifetracker.user.exception.FriendErrorCode
import com.benecia.lifetracker.user.exception.UserErrorCode
import com.benecia.lifetracker.user.service.Friend
import com.benecia.lifetracker.user.service.FriendRepository
import com.benecia.lifetracker.user.service.FriendStatus
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class FriendEntityRepository(
    private val friendJpaRepository: FriendJpaRepository,
) : FriendRepository {

    override fun add(friend: Friend): Long {
        val entity = FriendEntity.from(friend)
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

    override fun findPendingRequestsByReceiverId(receiverId: UUID): List<Friend> {
        return friendJpaRepository.findAllByReceiverIdAndStatus(receiverId, FriendStatus.PENDING)
            .map { it.toDomain() }
    }

    override fun findPendingRequestsByRequesterId(requesterId: UUID): List<Friend> {
        return friendJpaRepository.findAllByRequesterIdAndStatus(requesterId, FriendStatus.PENDING)
            .map { it.toDomain() }
    }

    override fun findFriendRequestById(friendRequestId: Long): Friend {
        return friendJpaRepository.findByIdOrNull(friendRequestId)
            ?.toDomain()
            ?: throw CoreException(FriendErrorCode.FRIEND_REQUEST_NOT_FOUND)
    }

    override fun changeFriendRequestStatus(friendRequestId: Long, status: FriendStatus): Long {
        val entity = friendJpaRepository.findByIdOrNull(friendRequestId)
            ?: throw CoreException(FriendErrorCode.FRIEND_REQUEST_NOT_FOUND)
        entity.status = status
        return friendJpaRepository.save(entity).id!!
    }

    @Transactional
    override fun delete(userId: UUID, friendId: Long) {
        val entity = friendJpaRepository.findByIdOrNull(friendId)
            ?: throw CoreException(FriendErrorCode.FRIEND_REQUEST_NOT_FOUND)
        if (entity.requesterId != userId && entity.receiverId != userId) {
            throw CoreException(UserErrorCode.FORBIDDEN_USER_ACCESS)
        }
        friendJpaRepository.delete(entity)
    }
}
