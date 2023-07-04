package com.service.kapai.repository

import com.service.kapai.repository.entity.TokenDestroyEntity
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TokenDestroyRepository : ListCrudRepository<TokenDestroyEntity, Long>, ListPagingAndSortingRepository<TokenDestroyEntity, Long> {

    fun findByTokenAndDay(token: String, day: LocalDate): TokenDestroyEntity?

}