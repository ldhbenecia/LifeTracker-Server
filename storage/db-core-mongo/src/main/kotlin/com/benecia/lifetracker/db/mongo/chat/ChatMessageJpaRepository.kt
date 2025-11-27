package com.benecia.lifetracker.db.mongo.chat

import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatMessageJpaRepository : MongoRepository<ChatMessageDocument, String> {
    fun findByRoomId(roomId: Long, pageable: Pageable): List<ChatMessageDocument>
    fun findByRoomIdAndTimestampLessThan(roomId: Long, timestamp: Long, pageable: Pageable): List<ChatMessageDocument>
    fun findByRoomIdAndTimestampLessThanEqual(roomId: Long, timestamp: Long?, pageable: Pageable): List<ChatMessageDocument>
}
