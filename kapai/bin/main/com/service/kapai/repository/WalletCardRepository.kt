package com.service.kapai.repository

import com.service.kapai.repository.entity.WalletCardEntity
import com.service.kapai.repository.model.WalletCardInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
interface WalletCardRepository : ListCrudRepository<WalletCardEntity, Long>, ListPagingAndSortingRepository<WalletCardEntity, Long> {

    @Query(
        """
SELECT `card_model`.`id`,
       `card_model`.`name`,
       `card_model`.`unit_price`,
       `card_model`.`power`,
       `card_model`.`output`,
       `card_model`.`ep`,
       `wallet_card`.`wallet_id`,
       MAX(`wallet_card`.`days`) as days,
       IFNULL(SUM(`wallet_card`.`power`), 0)  as `my_power`,
       IFNULL(SUM(`wallet_card`.`output`), 0) as `my_output`,
       IFNULL(COUNT(*), 0)                    as `quantity`
FROM `card_model`
         LEFT JOIN `wallet_card` ON `card_model`.`id` = `wallet_card`.`card_model_id`
WHERE `wallet_card`.`wallet_id`=:walletId
  AND `wallet_card`.`output` > 0
  AND `wallet_card`.`status` = 0
GROUP BY `wallet_card`.`card_model_id`
    """
    )
    fun findByWallet(walletId: Long): List<WalletCardInfo>

    fun existsByCardModelIdAndWalletId(cardModelId: Long, walletId: Long): Boolean

    @Query("SELECT IFNULL(SUM(`power`), 0) as power FROM `wallet_card` WHERE `output`>0 AND `status`=0")
    fun currentTotalPower(): Long

    @Query("SELECT IFNULL(SUM(`power`), 0) as power FROM `wallet_card` WHERE `wallet_id`=:walletId AND `output`>0 AND `status`=0")
    fun walletTotalPower(walletId: Long): Long

    fun findByStatusAndOutputGreaterThan(status: Int, output: BigDecimal, pageable: Pageable): Page<WalletCardEntity>

    fun findByWalletIdAndStatusAndOutputGreaterThan(walletId: Long, status: Int, output: BigDecimal): List<WalletCardEntity>

    fun findByCardModelIdAndWalletIdOrderByIdDesc(cardModelId: Long, walletId: Long, pageable: Pageable): Page<WalletCardEntity>

    fun findByWalletIdAndStatusAndDaysGreaterThanEqual(walletId: Long, status: Int, days: Int): List<WalletCardEntity>
}