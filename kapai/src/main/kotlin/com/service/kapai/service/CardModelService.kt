package com.service.kapai.service

import com.service.kapai.repository.CardModelRepository
import com.service.kapai.repository.entity.CardModelEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CardModelService(
    private val cardModelRepository: CardModelRepository
) {

    @Transactional(readOnly = true)
    fun findById(id: Long) = cardModelRepository.findByIdOrNull(id)

    @Transactional(readOnly = true)
    fun findAll() = cardModelRepository.findAll()

    @Transactional
    fun update(card: CardModelEntity) {
        cardModelRepository.save(card)
    }

}