package com.service.kapai.repository

import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository
import com.service.kapai.repository.entity.MoldOrderEntity

@Repository
interface MoldOrderRepository : ListCrudRepository<MoldOrderEntity, Long>, ListPagingAndSortingRepository<MoldOrderEntity, Long> {
}