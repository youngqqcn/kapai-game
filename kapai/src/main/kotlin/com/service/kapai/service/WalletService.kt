package com.service.kapai.service

import com.service.kapai.REDIS_KEY_ORDER
import com.service.kapai.REDIS_KEY_USER_CHILD_1
import com.service.kapai.configuration.AppProperties
import com.service.kapai.jna.LibWeb3
import com.service.kapai.jna.go.GoString
import com.service.kapai.repository.*
import com.service.kapai.repository.entity.*
import com.service.kapai.repository.model.Node
import com.service.kapai.repository.model.WalletCardInfo
import com.service.boot.exception.AppException
import com.service.boot.i18n.I18nMessage
import com.service.boot.number
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@Service
class WalletService(
    private val properties: AppProperties,
    private val i18nMessage: I18nMessage,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val walletRepository: WalletRepository,
    private val cardModelRepository: CardModelRepository,
    private val walletCardRepository: WalletCardRepository,
    private val relationRepository: RelationRepository,
    private val assetLogRepository: AssetLogRepository,
    private val withdrawOrderRepository: WithdrawOrderRepository,
    private val checkInRepository: CheckInRepository,
    private val walletPowerRepository: WalletPowerRepository,
    private val swapEPOrderRepository: SwapEPOrderRepository,
) {

    @Transactional(readOnly = true)
    fun findWallet(wallet: String) = walletRepository.findByWallet(wallet)

    @Transactional(readOnly = true)
    fun listWallet(page: Int, pageSize: Int) =
        walletRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Order.asc("id"))))

    @Transactional(readOnly = true)
    fun listRelation(page: Int, pageSize: Int) =
        relationRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Order.asc("walletId"))))

    @Transactional
    fun bindRelation(currentUserId: Long, bindWallet: String) {
        var relation = relationRepository.findByWalletId(currentUserId)
        if (relation != null) {
            throw AppException(i18nMessage.getMessage("recommender_is_bound"))
        }
        val su = walletRepository.findByWallet(bindWallet) ?: throw AppException(i18nMessage.getMessage("recommender_does_not_exist"))
        val suRelation = relationRepository.findByWalletId(su.id) ?: throw AppException("Referrer has no relationship chain")
        val suRelationPath = suRelation.relationPath.split(",").filter { it.isNotBlank() }
        if (suRelation.superiorId <= 0 || suRelationPath.isEmpty()) throw AppException("Referrer has no relationship chain")
        if (suRelationPath.contains("$currentUserId")) {
            throw AppException("Circular Referral Relationship")
        }
        relation = RelationEntity()
        relation.walletId = currentUserId
        relation.superiorId = su.id
        val paths = mutableListOf<String>()
        paths.add("${su.id}")
        paths.addAll(suRelationPath)
        relation.relationPath = paths.joinToString(",")
        relationRepository.insert(relation.walletId, relation.superiorId, relation.relationPath)
        redisTemplate.opsForSet().add(su.id.REDIS_KEY_USER_CHILD_1(), currentUserId)
    }

    @Transactional(readOnly = true)
    fun listWalletCard(page: Int, pageSize: Int): Page<WalletCardEntity> {
        return walletCardRepository.findByStatusAndOutputGreaterThan(
            WalletCardEntity.Status.RELEASING.status,
            BigDecimal.ZERO,
            PageRequest.of(page, pageSize, Sort.by(Sort.Order.asc("id")))
        )
    }

    @Transactional(readOnly = true)
    fun listByWalletCard(walletId: Long): List<WalletCardEntity> {
        return walletCardRepository.findByWalletIdAndStatusAndOutputGreaterThan(
            walletId,
            WalletCardEntity.Status.RELEASING.status,
            BigDecimal.ZERO
        )
    }

    @Transactional(readOnly = true)
    fun findWalletById(id: Long) = walletRepository.findByIdOrNull(id)

    @Transactional
    fun registerWallet(wallet: WalletEntity) {
        walletRepository.save(wallet)
    }

    @Transactional(readOnly = true)
    fun findWalletById(id: Collection<Long>) = walletRepository.findByIdIn(id)

    @Transactional(readOnly = true)
    fun findRelation(walletId: Long) = relationRepository.findByWalletId(walletId)

    @Transactional(readOnly = true)
    fun findRelation(wallet: String) = relationRepository.findByWallet(wallet)

    @Transactional(readOnly = true)
    fun findSuperior(walletId: Long) = walletRepository.findSuperior(walletId)

    @Transactional
    fun saveOrRelation(relation: RelationEntity) =
        relationRepository.insert(relation.walletId, relation.superiorId, relation.relationPath)

    @Transactional(readOnly = true)
    fun getMyCards(walletId: Long): List<WalletCardInfo> {
        val list = mutableListOf<WalletCardInfo>()
        val map = walletCardRepository.findByWallet(walletId).associateBy { it.id }
        val cards = walletCardRepository.findByWalletIdAndStatusAndDaysGreaterThanEqual(walletId, WalletCardEntity.Status.RELEASING.status, 20)
        val sumB = cards.groupBy { it.cardModelId }.mapValues { it.value.sumOf { WalletCardEntity.getPowerB(it) } }
        val all = cardModelRepository.findAll()
        for (c in all) {
            val wc = map[c.id]
            if (wc != null) {
                wc.status = 1
                wc.myPowerB = sumB.get(wc.id) ?: BigDecimal.ZERO
                list.add(wc)
            } else {
                var status = 0
                if (walletCardRepository.existsByCardModelIdAndWalletId(c.id, walletId)) {
                    status = -1
                }
                list.add(WalletCardInfo().also {
                    it.id = c.id
                    it.name = c.name
                    it.unitPrice = c.unitPrice
                    it.power = c.power
                    it.output = c.output
                    it.ep = c.ep
                    it.walletId = walletId
                    it.status = status
                })
            }
        }
        return list
    }

    @Transactional(readOnly = true)
    fun currentTotalPower() = walletCardRepository.currentTotalPower()

    @Transactional(readOnly = true)
    fun walletTotalPower(walletId: Long): Long {
        return walletCardRepository.walletTotalPower(walletId)
    }

    @Transactional
    fun saveOrUpdateWithdrawOrder(order: WithdrawOrderEntity) {
        withdrawOrderRepository.save(order)
    }

    @Transactional(readOnly = true)
    fun logs(
        type: Array<Int>,
        source: Array<Int>,
        walletId: Long,
        cardModelId: Long?,
        date: LocalDate?,
        page: Int
    ): Page<AssetLogEntity> {
        val types = type.map { AssetLogEntity.Type.valueOf(it).type }
        val sources = source.toSet()
        val pageable = PageRequest.of(page, 20)
        val count = assetLogRepository.logsCount(types, sources, walletId, date, cardModelId)
        val list = assetLogRepository.logs(types, sources, walletId, date, cardModelId, pageable.offset, pageable.pageSize).toList()
        return PageImpl(list, pageable, count)
    }

    @Transactional(readOnly = true)
    fun getSumAmount(walletId: Long, type: CheckInEntity.Type, status: CheckInEntity.Status, date: LocalDate) =
        checkInRepository.getSumAmount(walletId, type.type, status.status, date)

    @Transactional
    fun checkIn(type: CheckInEntity.Type, wallet: WalletEntity, value: BigDecimal): Boolean {
        val logType: AssetLogEntity.Type
        val token: String
        val tokenName : String
        when (type) {
            CheckInEntity.Type.TOKEN_A -> {
                token = properties.tokenA
                tokenName = "ART"
                logType = AssetLogEntity.Type.TOKEN_A
            }
            CheckInEntity.Type.TOKEN_B -> {
                token = properties.tokenB
                tokenName = "SOUL"
                logType = AssetLogEntity.Type.TOKEN_B
            }
            else -> return false
        }
        val txHash = LibWeb3.LIB_WEB_3.SendERC20Transfer(
            GoString.ByValue.of(properties.walletPrivateKey),
            GoString.ByValue.of(token),
            GoString.ByValue.of(wallet.wallet),
            GoString.ByValue.of(value.number()),
        )
        if (txHash.isNullOrBlank()) {
            return false
        }
        val log = AssetLogEntity()
        log.walletId = wallet.id
        log.type = logType.type
        log.source = AssetLogEntity.SourceToken.WITHDRAW.source
        log.amount = value.negate()
        log.beforeAmount = value
        log.afterAmount = BigDecimal.ZERO
        log.extra = txHash
        val order = WithdrawOrderEntity().also {
            it.walletId = wallet.id
            it.token = token
            it.tokenName = tokenName
            it.amount = value
            it.txHash = txHash
        }
        checkInRepository.updateStatus(wallet.id, type.type, CheckInEntity.Status.COMPLETED.status, LocalDate.now())
        withdrawOrderRepository.save(order)
        assetLogRepository.save(log)
        redisTemplate.opsForList().rightPush(REDIS_KEY_ORDER, order)
        return true
    }

    @Transactional(readOnly = true)
    fun getLastCard(cardId: Long, walletId: Long): WalletCardEntity? {
        val page = walletCardRepository.findByCardModelIdAndWalletIdOrderByIdDesc(cardId, walletId, PageRequest.of(0, 1))
        return page.content.firstOrNull()
    }

    @Transactional(readOnly = true)
    fun getYesterdaySmallPower(walletId: Long): Long {
        return walletPowerRepository.findByIdOrNull(walletId)?.smallPower ?: 0
    }

    @Transactional
    fun saveAssetLogs(logs: Collection<AssetLogEntity>) = assetLogRepository.saveAll(logs)

    @Transactional(readOnly = true)
    fun countByNode(node: Node) = walletRepository.countByNode(node.node)

    @Transactional(readOnly = true)
    fun findSwapEPOrder(orderId: String) = swapEPOrderRepository.findByOrderId(orderId)

    @Transactional
    fun saveSwapOrder(order: SwapEPOrderEntity) {
        swapEPOrderRepository.save(order)
    }

    @Transactional(readOnly = true)
    fun getSumCheckAmountTotal(type: CheckInEntity.Type, date: LocalDate): BigDecimal {
        return checkInRepository.getSumCheckAmountTotal(type.type, date)
    }
}