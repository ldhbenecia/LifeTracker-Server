package com.benecia.lifetracker.db.jpa.memo

import com.benecia.lifetracker.memocore.service.Memo
import com.benecia.lifetracker.memocore.service.MemoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class MemoEntityRepository(
    private val memoJpaRepository: MemoJpaRepository,
) : MemoRepository {

    override fun findByUserIdAndId(userId: UUID, id: Long): Memo? = memoJpaRepository.findByUserIdAndId(userId, id)?.toDomain()

    override fun findAllByUserId(userId: UUID): List<Memo> = memoJpaRepository.findAllByUserIdOrderByCreatedAtDesc(userId).map { it.toDomain() }

    override fun add(memo: Memo): Long = memoJpaRepository.save(MemoEntity.from(memo)).id!!

    @Transactional
    override fun modify(id: Long, memo: Memo): Long? {
        val entity = memoJpaRepository.findByIdOrNull(id) ?: return null
        entity.categoryId = memo.categoryId
        entity.title = memo.title
        entity.content = memo.content
        entity.tags = memo.tags.toMutableList()
        return memoJpaRepository.save(entity).id
    }

    override fun remove(id: Long): Long? {
        val entity = memoJpaRepository.findByIdOrNull(id) ?: return null
        entity.remove()
        return memoJpaRepository.save(entity).id
    }
}
