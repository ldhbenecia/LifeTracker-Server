package com.benecia.lifetracker.db.jpa.memo

import com.benecia.lifetracker.db.jpa.BaseEntity
import com.benecia.lifetracker.memocore.service.Memo
import com.benecia.lifetracker.memocore.service.MemoStatus
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@SQLRestriction("status = 'ACTIVE'")
@Table(name = "memo")
class MemoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val userId: UUID,

    var categoryId: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var content: String? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "memo_tag", joinColumns = [JoinColumn(name = "memo_id")])
    @Column(name = "tag", nullable = false)
    var tags: MutableList<String> = mutableListOf(),

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: MemoStatus = MemoStatus.ACTIVE,
) : BaseEntity() {

    companion object {
        fun from(memo: Memo): MemoEntity = MemoEntity(
            id = memo.id,
            userId = memo.userId,
            categoryId = memo.categoryId,
            title = memo.title,
            content = memo.content,
            tags = memo.tags.toMutableList(),
            status = memo.status,
        )
    }

    fun toDomain(): Memo = Memo(
        id = id,
        userId = userId,
        categoryId = categoryId,
        title = title,
        content = content,
        tags = tags.toList(),
        status = status,
    )

    fun remove() {
        this.status = MemoStatus.DELETED
    }
}
