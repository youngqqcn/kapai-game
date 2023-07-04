package com.service.kapai.repository

import com.service.kapai.repository.entity.RelationEntity
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface RelationRepository : ListCrudRepository<RelationEntity, Long>, ListPagingAndSortingRepository<RelationEntity, Long> {

    @Modifying
    @Query("INSERT INTO `relation`(`wallet_id`, `superior_id`, `relation_path`) VALUES (:walletId, :superiorId, :relationPath)")
    fun insert(walletId: Long, superiorId: Long, relationPath: String)

    fun findByWalletId(id: Long): RelationEntity?

    @Query("SELECT `relation`.* FROM `relation` INNER JOIN `wallet` on `relation`.`wallet_id` = `wallet`.`id` WHERE `wallet`.`wallet`=:wallet")
    fun findByWallet(wallet: String): RelationEntity?

}