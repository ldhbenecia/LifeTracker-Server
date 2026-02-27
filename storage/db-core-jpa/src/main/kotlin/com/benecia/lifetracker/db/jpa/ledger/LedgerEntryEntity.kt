package com.benecia.lifetracker.db.jpa.ledger

import com.benecia.lifetracker.db.jpa.BaseEntity
import com.benecia.lifetracker.ledgercore.service.LedgerEntry
import com.benecia.lifetracker.ledgercore.service.LedgerType
import com.benecia.lifetracker.ledgercore.service.PaymentMethod
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "ledger_entry")
class LedgerEntryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val userId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: LedgerType,

    @Column(nullable = false)
    var amount: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var paymentMethod: PaymentMethod,

    @Column(nullable = true)
    var categoryId: Long?,

    @Column(nullable = true)
    var memo: String?,

    @Column(nullable = false)
    var transactionDate: LocalDate,

) : BaseEntity() {

    companion object {
        fun from(entry: LedgerEntry): LedgerEntryEntity {
            return LedgerEntryEntity(
                id = entry.id,
                userId = entry.userId,
                type = entry.type,
                amount = entry.amount,
                paymentMethod = entry.paymentMethod,
                categoryId = entry.categoryId,
                memo = entry.memo,
                transactionDate = entry.transactionDate,
            )
        }
    }

    fun toDomain(): LedgerEntry = LedgerEntry(
        id = this.id,
        userId = this.userId,
        type = this.type,
        amount = this.amount,
        paymentMethod = this.paymentMethod,
        categoryId = this.categoryId,
        memo = this.memo,
        transactionDate = this.transactionDate,
    )
}
