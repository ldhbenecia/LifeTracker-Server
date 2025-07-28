package com.benecia.lifetracker.db.jpa.user

import com.benecia.lifetracker.db.jpa.BaseEntity
import com.benecia.lifetracker.user.service.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "user")
class UserEntity(

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID? = null,

    @Column(nullable = false)
    val provider: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    var displayName: String,

    @Column(nullable = false)
    var profileImageUrl: String?,

) : BaseEntity() {

    fun toDomain(): User = User(
        id = this.id!!,
        provider = this.provider,
        email = this.email,
        displayName = this.displayName,
        profileImageUrl = this.profileImageUrl,
    )
}
