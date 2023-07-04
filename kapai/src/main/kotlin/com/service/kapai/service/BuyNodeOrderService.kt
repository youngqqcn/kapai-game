package com.service.kapai.service

import com.service.kapai.repository.AssetLogRepository
import com.service.kapai.repository.BuyNodeOrderRepository
import com.service.kapai.repository.WalletRepository
import com.service.kapai.repository.entity.AssetLogEntity
import com.service.kapai.repository.entity.BuyNodeOrderEntity
import com.service.kapai.repository.entity.WalletEntity
import com.service.kapai.repository.model.TransactionStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BuyNodeOrderService(
    private val walletRepository: WalletRepository,
    private val buyNodeOrderRepository: BuyNodeOrderRepository,
    private val assetLogRepository: AssetLogRepository,
) {

    @Transactional(readOnly = true)
    fun findBuyNodeOrder(orderId: Long) = buyNodeOrderRepository.findByIdOrNull(orderId)

    @Transactional
    fun saveOrUpdateNodeOrder(order: BuyNodeOrderEntity, wallet: WalletEntity?,logs: Collection<AssetLogEntity>?) {
        buyNodeOrderRepository.save(order)
        if (wallet != null) walletRepository.save(wallet)
        if (!logs.isNullOrEmpty()) assetLogRepository.saveAll(logs)
    }

    @Transactional(readOnly = true)
    fun getWalletBuyNodeSumPrice(walletId: Long): Long {
        return buyNodeOrderRepository.getWalletBuyNodeSumPrice(walletId)?.sumOf { it.price } ?: 0
    }

    @Transactional(readOnly = true)
    fun getOrders(walletId: Long, pageSize: Int, page: Int): Page<BuyNodeOrderEntity> {
        return buyNodeOrderRepository.findByWalletIdAndStatusIn(walletId, listOf(TransactionStatus.IN_PROGRESS, TransactionStatus.COMPLETED), PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("id"))))
    }
}