package com.service.kapai.repository

import com.service.kapai.repository.entity.WalletPowerEntity
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletPowerRepository  : ListCrudRepository<WalletPowerEntity, Long>, ListPagingAndSortingRepository<WalletPowerEntity, Long> {
}