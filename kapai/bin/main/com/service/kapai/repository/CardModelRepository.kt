package com.service.kapai.repository

import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository
import com.service.kapai.repository.entity.CardModelEntity

@Repository
interface CardModelRepository : ListCrudRepository<CardModelEntity, Long>, ListPagingAndSortingRepository<CardModelEntity, Long> {


}