package com.benecia.lifetracker.db.jpa.ledger

import com.benecia.lifetracker.db.jpa.BaseEntity
import com.benecia.lifetracker.ledgercore.service.LedgerCategory
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
    name = "ledger_category",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "name"])],
)
class LedgerCategoryEntity(

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
        fun from(category: LedgerCategory): LedgerCategoryEntity {
            return LedgerCategoryEntity(
                id = category.id,
                userId = category.userId,
                name = category.name,
                icon = category.icon,
                color = category.color,
            )
        }
    }

    fun toDomain(): LedgerCategory = LedgerCategory(
        id = this.id,
        userId = this.userId,
        name = this.name,
        icon = this.icon,
        color = this.color,
    )
}
