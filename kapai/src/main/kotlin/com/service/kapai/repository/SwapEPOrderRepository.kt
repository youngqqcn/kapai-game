package com.service.kapai.repository

import com.service.kapai.repository.entity.SwapEPOrderEntity
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface SwapEPOrderRepository : ListCrudRepository<SwapEPOrderEntity, Long>, ListPagingAndSortingRepository<SwapEPOrderEntity, Long> {

    fun findByOrderId(orderId: String): SwapEPOrderEntity?

}