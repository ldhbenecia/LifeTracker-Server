package com.benecia.lifetracker.user.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FriendRepository {
    fun add(friend: Friend): Long
    fun exists(requesterId: UUID, receiverId: UUID): Boolean
    fun findAllByUserId(userId: UUID): List<Friend>
    fun findPendingRequestsByReceiverId(receiverId: UUID): List<Friend>
    fun findPendingRequestsByRequesterId(requesterId: UUID): List<Friend>
    fun findFriendRequestById(friendRequestId: Long): Friend
    fun changeFriendRequestStatus(friendRequestId: Long, status: FriendStatus): Long
    fun delete(userId: UUID, friendId: Long)
}
