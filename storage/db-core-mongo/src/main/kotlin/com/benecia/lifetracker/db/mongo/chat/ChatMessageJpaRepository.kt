package com.benecia.lifetracker.db.mongo.chat

import org.springframework.data.mongodb.repository.MongoRepository

interface ChatMessageJpaRepository : MongoRepository<ChatMessageDocument, String> {
    fun findAllByRoomIdOrderByTimestampAsc(roomId: String): List<ChatMessageDocument>
}
