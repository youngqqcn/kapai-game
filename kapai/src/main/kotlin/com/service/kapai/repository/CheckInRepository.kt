package com.service.kapai.repository

import com.service.kapai.repository.entity.CheckInEntity
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
interface CheckInRepository : ListCrudRepository<CheckInEntity, Long>, ListPagingAndSortingRepository<CheckInEntity, Long> {

    @Query("SELECT IFNULL(SUM(`amount`), 0) AS `amount` FROM `check_in` WHERE `wallet_id`=:walletId AND `type`=:type AND `status`=:status AND `date`=:date")
    fun getSumAmount(walletId: Long, type: Int, status: Int, date: LocalDate): BigDecimal

    @Modifying
    @Query("UPDATE `check_in` SET `status`=:status WHERE `wallet_id`=:walletId AND `type`=:type AND `date`=:date")
    fun updateStatus(walletId: Long, type: Int, status: Int, date: LocalDate)

    @Query("SELECT IFNULL(SUM(`amount`), 0) AS `amount` FROM `check_in` WHERE `type`=:type AND `date`=:date")
    fun getSumCheckAmountTotal(type: Int, date: LocalDate): BigDecimal

}