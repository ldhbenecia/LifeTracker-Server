package com.benecia.lifetracker.db.jpa.memo

import com.benecia.lifetracker.db.jpa.BaseEntity
import com.benecia.lifetracker.memocore.service.MemoCategory
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.UUID

@Entity
@Table(
    name = "memo_category",
    uniqueConstraints = [UniqueConstraint(columnNames = ["userId", "name"])],
)
class MemoCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var icon: String,

    @Column(nullable = false)
    var color: String,
) : BaseEntity() {

    companion object {
        fun from(category: MemoCategory): MemoCategoryEntity = MemoCategoryEntity(
            id = category.id,
            userId = category.userId,
            name = category.name,
            icon = category.icon,
            color = category.color,
        )
    }

    fun toDomain(): MemoCategory = MemoCategory(id = id, userId = userId, name = name, icon = icon, color = color)
}
