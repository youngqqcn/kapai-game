package com.service.kapai.repository

import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository
import com.service.kapai.repository.entity.WalletEntity
import com.service.kapai.repository.model.Node
import org.springframework.data.jdbc.repository.query.Query
import java.math.BigDecimal

@Repository
interface WalletRepository : ListCrudRepository<WalletEntity, Long>, ListPagingAndSortingRepository<WalletEntity, Long> {

    fun findByWallet(wallet: String): WalletEntity?

    @Query("SELECT `wallet`.* FROM `wallet` INNER JOIN `relation` on `wallet`.`id` = `relation`.`superior_id` WHERE `relation`.`wallet_id`=:walletId;")
    fun findSuperior(walletId: Long): WalletEntity?

    fun findByIdIn(id: Collection<Long>): List<WalletEntity>

    fun countByNode(node: Node): Int

    @Query("SELECT IFNULL(SUM(`lock_token_a`), 0) AS `amount` FROM `wallet` WHERE `node`>0")
    fun sumLockTokenA(): BigDecimal
}