package com.service.kapai.service

import com.service.kapai.repository.LevelModelRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LevelModelService(
    private val levelModelRepository: LevelModelRepository
) {

    @Transactional(readOnly = true)
    fun findAll() = levelModelRepository.findAll()

}