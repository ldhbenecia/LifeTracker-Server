package com.benecia.lifetracker.db.jpa

import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@MappedSuperclass
abstract class BaseEntity {
    @CreationTimestamp
    val createdAt: ZonedDateTime? = null

    @UpdateTimestamp
    var updatedAt: ZonedDateTime? = null
        protected set
}
