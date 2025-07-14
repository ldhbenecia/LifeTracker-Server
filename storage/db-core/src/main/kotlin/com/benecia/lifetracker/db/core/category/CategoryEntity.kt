package com.benecia.lifetracker.db.core.category

import com.benecia.lifetracker.db.core.BaseEntity
import com.benecia.lifetracker.todocore.service.Category
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
    name = "category",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "name"])],
)
class CategoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val icon: String,

    @Column(nullable = false)
    val color: String,

) : BaseEntity() {

    companion object {
        fun from(category: Category): CategoryEntity {
            return CategoryEntity(
                category.id,
                category.userId,
                category.name,
                category.icon,
                category.color,
            )
        }
    }

    fun toDomain(): Category = Category(
        id = this.id,
        userId = this.userId,
        name = this.name,
        icon = this.icon,
        color = this.color,
    )
}
