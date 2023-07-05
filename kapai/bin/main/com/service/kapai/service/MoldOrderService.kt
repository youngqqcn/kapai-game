package com.service.kapai.service

import com.service.kapai.repository.*
import com.service.kapai.repository.entity.AssetLogEntity
import com.service.kapai.repository.entity.CheckInEntity
import com.service.kapai.repository.entity.MoldOrderEntity
import com.service.kapai.repository.entity.WalletCardEntity
import com.service.kapai.repository.entity.WalletEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MoldOrderService(
    private val walletRepository: WalletRepository,
    private val walletCardRepository: WalletCardRepository,
    private val moldOrderRepository: MoldOrderRepository,
    private val assetLogRepository: AssetLogRepository,
    private val checkInRepository: CheckInRepository
) {

    @Transactional
    fun saveOrUpdateMoldOrder(entity: MoldOrderEntity, wallet: WalletEntity?, log: AssetLogEntity?) {
        moldOrderRepository.save(entity)
        if (wallet != null) walletRepository.save(wallet)
        if (log != null) {
            assetLogRepository.save(log)
        }
    }

    @Transactional(readOnly = true)
    fun findMoldOrderById(orderId: Long) = moldOrderRepository.findByIdOrNull(orderId)

    @Transactional
    fun onCasting(entity: MoldOrderEntity, sw: WalletEntity?, wcs: Collection<WalletCardEntity>?, cw: WalletEntity?, logs: Collection<AssetLogEntity>?, checkIn: Collection<CheckInEntity>?) {
        moldOrderRepository.save(entity)
        if (sw != null) walletRepository.save(sw)
        if (!wcs.isNullOrEmpty()) walletCardRepository.saveAll(wcs)
        if (cw != null) walletRepository.save(cw)
        if (!logs.isNullOrEmpty()) assetLogRepository.saveAll(logs)
        if (!checkIn.isNullOrEmpty()) checkInRepository.saveAll(checkIn)
    }
}