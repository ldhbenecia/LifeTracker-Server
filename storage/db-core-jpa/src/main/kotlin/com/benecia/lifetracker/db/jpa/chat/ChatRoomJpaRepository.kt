package com.benecia.lifetracker.db.jpa.chat

import com.benecia.lifetracker.chat.chatRoom.service.ChatRoomWithOpponent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface ChatRoomJpaRepository : JpaRepository<ChatRoomEntity, Long> {

    @Query("""
        SELECT r FROM ChatRoomEntity r
        JOIN ChatRoomUserEntity cu ON r.id = cu.id.roomId
        WHERE cu.id.userId = :userId AND cu.visible = true
        ORDER BY r.lastMessageTime DESC
    """)
    fun findAllRoomsByUserId(@Param("userId") userId: UUID): List<ChatRoomEntity>

    @Query("""
        SELECT new com.benecia.lifetracker.chat.chatRoom.service.ChatRoomWithOpponent(
            r.id, r.lastMessage, r.lastMessageTime,
            u.id, u.displayName, u.profileImageUrl
        )
        FROM ChatRoomEntity r
        JOIN ChatRoomUserEntity cu1 ON r.id = cu1.id.roomId AND cu1.id.userId = :userId AND cu1.visible = true
        JOIN ChatRoomUserEntity cu2 ON r.id = cu2.id.roomId AND cu2.id.userId != :userId
        JOIN UserEntity u ON cu2.id.userId = u.id
        ORDER BY r.lastMessageTime DESC
    """)
    fun findRoomsWithOpponents(@Param("userId") userId: UUID): List<ChatRoomWithOpponent>
}