package com.service.kapai.service

import com.service.kapai.repository.TokenDestroyRepository
import com.service.kapai.repository.entity.TokenDestroyEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class TokenDestroyService(
    private val tokenDestroyRepository: TokenDestroyRepository
) {

    @Transactional(readOnly = true)
    fun findByTokenAndDay(token: String, day: LocalDate) = tokenDestroyRepository.findByTokenAndDay(token, day)

    @Transactional
    fun save(entity: TokenDestroyEntity) = tokenDestroyRepository.save(entity)

}