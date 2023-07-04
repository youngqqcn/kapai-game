package com.service.kapai.repository

import com.service.kapai.repository.entity.WithdrawOrderEntity
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface WithdrawOrderRepository : ListCrudRepository<WithdrawOrderEntity, Long>, ListPagingAndSortingRepository<WithdrawOrderEntity, Long> {}