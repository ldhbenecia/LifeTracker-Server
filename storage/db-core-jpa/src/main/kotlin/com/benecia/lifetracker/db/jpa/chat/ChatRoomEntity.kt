package com.benecia.lifetracker.db.jpa.chat

import com.benecia.lifetracker.db.jpa.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "chat_room")
class ChatRoomEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var lastMessage: String? = null,

    var lastMessageTime: LocalDateTime? = null

): BaseEntity()