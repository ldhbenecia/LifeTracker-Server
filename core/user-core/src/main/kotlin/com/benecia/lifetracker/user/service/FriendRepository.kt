package com.benecia.lifetracker.user.service

import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FriendRepository {
    fun add(requesterId: UUID, receiverId: UUID): Long
    fun exists(requesterId: UUID, receiverId: UUID): Boolean
    fun findAllByUserId(userId: UUID): List<Friend>
}
