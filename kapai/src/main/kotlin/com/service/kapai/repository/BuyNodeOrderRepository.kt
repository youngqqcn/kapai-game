package com.service.kapai.repository

import com.service.kapai.repository.entity.BuyNodeOrderEntity
import com.service.kapai.repository.model.BuyNodeCount
import com.service.kapai.repository.model.TransactionStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface BuyNodeOrderRepository : ListCrudRepository<BuyNodeOrderEntity, Long>, ListPagingAndSortingRepository<BuyNodeOrderEntity, Long> {
    @Query("SELECT `node`, SUM(`price`) as `price` FROM `buy_node_order` WHERE `wallet_id`=:walletId AND `status`=1 GROUP BY `node`")
    fun getWalletBuyNodeSumPrice(walletId: Long): List<BuyNodeCount>?

    fun findByWalletIdAndStatusIn(walletId: Long, status: Collection<TransactionStatus>, pageable: Pageable): Page<BuyNodeOrderEntity>
}