package com.benecia.lifetracker.db.jpa.chat

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface ChatRoomUserJpaRepository : JpaRepository<ChatRoomUserEntity, ChatRoomUserId> {

    @Query(
        """
        SELECT cu.id.roomId
        FROM ChatRoomUserEntity cu
        WHERE cu.id.userId IN (:userId1, :userId2) AND cu.visible = true
        GROUP BY cu.id.roomId
        HAVING COUNT(cu.id.userId) = 2
    """,
    )
    fun findRoomIdByUserIds(@Param("userId1") userId1: UUID, @Param("userId2") userId2: UUID): Long?
}
