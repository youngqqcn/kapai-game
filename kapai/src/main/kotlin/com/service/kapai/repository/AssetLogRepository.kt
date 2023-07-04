package com.service.kapai.repository

import com.service.kapai.repository.entity.AssetLogEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.data.repository.query.ListQueryByExampleExecutor
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*
import java.util.stream.Stream

@Repository
interface AssetLogRepository : ListCrudRepository<AssetLogEntity, Long>,
    ListPagingAndSortingRepository<AssetLogEntity, Long>,
    ListQueryByExampleExecutor<AssetLogEntity> {

    @Query("""
SELECT *
FROM `asset_log`
WHERE `type` IN (:type)
  AND `source` IN (:source)
  AND wallet_id = :walletId
  AND IF(:createTime IS NOT NULL, DATE(`create_time`) = :createTime, TRUE)
  AND IF(:cardModelId IS NOT NULL, `card_model_id` = :cardModelId, TRUE)
  ORDER BY `create_time` DESC LIMIT :offset,:pageSize;
    """)
    fun logs(
        type: Collection<AssetLogEntity.Type>,
        source: Collection<Int>,
        walletId: Long,
        createTime: LocalDate?,
        cardModelId: Long?,
        offset: Long,
        pageSize: Int
    ): Stream<AssetLogEntity>

    @Query("""
SELECT COUNT(*) AS count
FROM `asset_log`
WHERE `type` IN (:type)
  AND `source` IN (:source)
  AND wallet_id = :walletId
  AND IF(:createTime IS NOT NULL, DATE(`create_time`) = :createTime, TRUE)
  AND IF(:cardModelId IS NOT NULL, `card_model_id` = :cardModelId, TRUE);
    """)
    fun logsCount(
        type: Collection<AssetLogEntity.Type>,
        source: Collection<Int>,
        walletId: Long,
        createTime: LocalDate?,
        cardModelId: Long?
    ): Long
}