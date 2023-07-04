package com.service.kapai.task

import com.service.boot.common.DateTimeFormat
import com.service.boot.common.NumberUtils.乘
import com.service.boot.common.NumberUtils.减
import com.service.boot.common.NumberUtils.加
import com.service.boot.common.NumberUtils.大于
import com.service.boot.common.NumberUtils.大于等于
import com.service.boot.common.NumberUtils.小于
import com.service.boot.common.NumberUtils.小于等于
import com.service.boot.common.NumberUtils.除
import com.service.boot.json.JSON
import com.service.boot.number
import com.service.kapai.*
import com.service.kapai.configuration.AppProperties
import com.service.kapai.jna.LibWeb3
import com.service.kapai.jna.go.GoString
import com.service.kapai.repository.entity.*
import com.service.kapai.repository.model.Node
import com.service.kapai.repository.model.TransactionStatus
import com.service.kapai.service.*
import org.slf4j.LoggerFactory
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.Page
import org.springframework.data.redis.connection.RedisServerCommands
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.jdbc.datasource.init.ScriptUtils
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.math.BigDecimal
import java.sql.Connection
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import kotlin.math.min

@Component
class Tasks(
    private val properties: AppProperties,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val jdbcTemplate: JdbcTemplate,
    private val walletService: WalletService,
    private val levelModelService: LevelModelService,
    private val cardModelService: CardModelService,
    private val moldOrderService: MoldOrderService,
    private val buyNodeOrderService: BuyNodeOrderService,
    private val tokenDestroyService: TokenDestroyService
) {
    private val logger = LoggerFactory.getLogger(Tasks::class.java)

    private var levels = emptyMap<Long, LevelModelEntity>()

    private var cards = emptyMap<Long, CardModelEntity>()

    @Scheduled(cron = "0 10 0 * * ?")//凌晨10分（00:10）
    //@Scheduled(cron = "0 10 5 * * ?")//凌晨5点10分（05:00）
    //@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
    fun statisticsJob() {
        statistics()
    }

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    fun updatePriceJob() {
        updatePrice()
    }

    @Async
    @Scheduled(cron = "0 10 8 * * ?")
    fun release() {
        val hash = LibWeb3.LIB_WEB_3.Release(
            GoString.ByValue.of(properties.walletPrivateKey),
            GoString.ByValue.of(properties.releaseContract)
        )
        if (hash.isNullOrBlank()) {
            logger.error("【合约交易】每日释放失败")
        } else {
            logger.info("【合约交易】每日释放hash: {}", hash)
        }
    }

    @Async
    fun startCheckCastingTransaction() {
        while (true) {
            if (Thread.currentThread().isInterrupted) {
                return
            }
            try {
                Thread.sleep(6000)
            } catch (e: Exception) {
                continue
            }
            if (Thread.currentThread().isInterrupted) {
                return
            }
            val operations = redisTemplate.opsForList()
            try {
                val orders = operations.range(REDIS_KEY_ORDER, 0, 100) ?: continue
                for (order in orders) {
                    when (order) {
                        is WithdrawOrderEntity -> {
                            if (order.txHash.isNullOrBlank() || order.status != TransactionStatus.IN_PROGRESS) {
                                operations.remove(REDIS_KEY_ORDER, 0, order)
                                continue
                            }
                            val json = LibWeb3.LIB_WEB_3.GetERC20TransferLog(GoString.ByValue.of(order.txHash))
                            logger.info("check erc20 hash: {}, receipt log: {}", order.txHash, json)
                            if (json != null) {
                                val obj = JSON.parseObject(json)
                                if (obj != null) {
                                    operations.remove(REDIS_KEY_ORDER, 0, order)
                                    val status = obj.getInteger("status")
                                    if (status == 1) {
                                        order.status = TransactionStatus.COMPLETED
                                    } else {
                                        order.status = TransactionStatus.FAILURE
                                    }
                                    walletService.saveOrUpdateWithdrawOrder(order)
                                }
                            }
                        }

                        is MoldOrderEntity -> {
                            if (order.txHash.isNullOrBlank() || order.status != TransactionStatus.IN_PROGRESS) {
                                operations.remove(REDIS_KEY_ORDER, 0, order)
                                continue
                            }
                            val json = LibWeb3.LIB_WEB_3.GetMoldLog(GoString.ByValue.of(order.txHash))
                            logger.info("check mold hash: {}, receipt log: {}", order.txHash, json)
                            if (json != null) {
                                val obj = JSON.parseObject(json)
                                if (obj != null) {
                                    operations.remove(REDIS_KEY_ORDER, 0, order)
                                    val status = obj.getInteger("status")
                                    val account = obj.getStringValue("account")
                                    val id = obj.getLongValue("id")
                                    val no = obj.getLongValue("no")
                                    val price = obj.getIntegerValue("price")
                                    val tokenA = BigDecimal(obj.getString("value") ?: "0")
                                    val ep = obj.getIntegerValue("ep")
                                    var sw: WalletEntity? = null //推荐人
                                    val wcs = mutableListOf<WalletCardEntity>() //铸造人铸造的卡牌数据
                                    val logs = mutableListOf<AssetLogEntity>()
                                    val checkIn = mutableListOf<CheckInEntity>()
                                    if (status == 1 && no == order.id) {
                                        order.status = TransactionStatus.COMPLETED
                                        order.price = price.toBigDecimal()
                                        order.ep = ep.toBigDecimal()
                                        order.tokenA = tokenA
                                        val card = JSON.parseObject(order.cardData, CardModelEntity::class.java)!!
//                                        if (price != card.unitPrice) {
//                                            card.unitPrice = price
//                                            if (order.type == 1 && ep != card.ep){
//                                                card.ep = ep
//                                            }
//                                            cardModelService.update(card)
//                                        }
                                        //铸造人增加算力
                                        var days = 0
                                        val last = walletService.getLastCard(id, order.walletId)
                                        if (last != null) {
                                            days = last.days
                                        }
                                        wcs.add(WalletCardEntity().also {
                                            it.cardModelId = id
                                            it.walletId = order.walletId
                                            it.output = BigDecimal(card.output)
                                            it.power = card.power.toLong()
                                            it.days = days
                                            it.status = WalletCardEntity.Status.RELEASING
                                        })
                                        logs.add(AssetLogEntity().also {
                                            it.walletId = order.walletId
                                            it.cardModelId = id
                                            it.type = AssetLogEntity.Type.Power
                                            it.source = AssetLogEntity.SourcePower.MOLD.source
                                            it.amount = card.power.toBigDecimal()
                                            it.extra = """{"orderId":${order.id}}"""
                                        })

                                        //获取推荐关系
                                        val relation = walletService.findRelation(account.lowercase())
                                        if (relation != null) {
                                            //获取铸造人的推荐人
                                            sw = walletService.findWalletById(relation.superiorId)
                                            if (sw != null) {
                                                //推荐人增加算力
                                                logs.add(AssetLogEntity().also {
                                                    it.walletId = sw.id
                                                    it.type = AssetLogEntity.Type.Power
                                                    it.source = AssetLogEntity.SourcePower.ZT_MOLD.source
                                                    it.cardModelId = id
                                                    it.beforeAmount = sw.ztPower.toBigDecimal()
                                                    it.amount = card.ztPower.toBigDecimal()
                                                    sw.ztPower += card.ztPower //推荐人增加算力
                                                    it.afterAmount = sw.ztPower.toBigDecimal()
                                                    it.extra = """{"orderId":${order.id}}"""
                                                })

                                                //私募代币释放
                                                if (sw.lockTokenA.大于(BigDecimal.ZERO)) {
                                                    var p1 = (price * Node.CARD_POWER_RATIO).toBigDecimal()
                                                    val p2 = sw.lockTokenA
                                                    if (p2.小于(p1)) {
                                                        p1 = p2
                                                    }
                                                    sw.lockTokenA = sw.lockTokenA.减(p1)
                                                    checkIn.add(CheckInEntity().also {
                                                        it.amount = p1
                                                        it.walletId = sw.id
                                                        it.date = LocalDate.now()
                                                        it.type = CheckInEntity.Type.TOKEN_A
                                                        it.status = CheckInEntity.Status.IN_PROGRESS
                                                    })
                                                    logs.add(
                                                        AssetLogEntity().also {
                                                            it.walletId = sw.id
                                                            it.type = AssetLogEntity.Type.TOKEN_A
                                                            it.source = AssetLogEntity.SourceToken.NODE_MOLD.source
                                                            it.cardModelId = id
                                                            it.beforeAmount = p2
                                                            it.amount = p1
                                                            it.afterAmount = sw.lockTokenA
                                                            it.extra = """{"orderId":${order.id}}"""
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        order.status = TransactionStatus.FAILURE
                                    }
                                    moldOrderService.onCasting(order, sw, wcs, null, logs, checkIn)
                                    syncWalletToRedis(null, order.walletId)
                                    syncWalletCardToRedis(order.walletId)
                                    if (sw != null) {
                                        syncWalletToRedis(null, sw.id)
                                    }
                                }
                            }
                        }

                        is BuyNodeOrderEntity -> {
                            if (order.txHash.isNullOrBlank() || order.status != TransactionStatus.IN_PROGRESS) {
                                operations.remove(REDIS_KEY_ORDER, 0, order)
                                continue
                            }
                            val json = LibWeb3.LIB_WEB_3.GetBuyNodeLog(GoString.ByValue.of(order.txHash))
                            logger.info("check buy node hash: {}, receipt log: {}", order.txHash, json)
                            if (json != null) {
                                val obj = JSON.parseObject(json)
                                if (obj != null) {
                                    operations.remove(REDIS_KEY_ORDER, 0, order)
                                    val status = obj.getInteger("status")
                                    if (status == 1) {
                                        val account = obj.getStringValue("account").lowercase()
                                        val wallet = walletService.findWallet(account)
                                        val node = Node.valueOfNode(obj.getIntegerValue("node"))
                                        val period = obj.getIntegerValue("period")
                                        val price = obj.getIntegerValue("price")
                                        val ep = obj.getIntegerValue("ep")
                                        if (node != Node.NODE_0 && wallet != null) {
                                            order.node = node
                                            order.price = price
                                            order.period = period
                                            order.ep = ep
                                            order.status = TransactionStatus.COMPLETED
                                            val logs = mutableListOf<AssetLogEntity>()
                                            val log = AssetLogEntity().also {
                                                it.walletId = wallet.id
                                                it.type = AssetLogEntity.Type.TOKEN_A
                                                it.source = AssetLogEntity.SourceToken.BUY_NODE.source
                                                it.beforeAmount = wallet.lockTokenA
                                                it.amount = price.toBigDecimal()
                                            }
                                            wallet.lockTokenA = wallet.lockTokenA.加(node.tokenA.toBigDecimal())
                                            if (wallet.node.node < node.node) {
                                                wallet.node = node
                                            }
                                            log.afterAmount = wallet.lockTokenA
                                            logs.add(log)
                                            buyNodeOrderService.saveOrUpdateNodeOrder(order, wallet, logs)
                                            syncWalletToRedis(wallet, null)
                                        }
                                    } else {
                                        order.status = TransactionStatus.FAILURE
                                        buyNodeOrderService.saveOrUpdateNodeOrder(order, null, null)
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * https://blog.csdn.net/whatday/article/details/103309081
     */
    @Async
    fun statistics() {
        levels = levelModelService.findAll().also { it.sortBy { it.id } }.associateBy { it.id }
        cards = cardModelService.findAll().also { it.sortBy { it.id } }.associateBy { it.id }
        val adv = getTodayDestroyValue(properties.tokenA)//A代币今日销毁
        val bdv = getTodayDestroyValue(properties.tokenB)//B代币今日销毁
        val acv = getCirculationValue(properties.tokenA)//A代币全网流通
        val bcv = getCirculationValue(properties.tokenB)//B代币全网流通
        val todayTotalA = BigDecimal("192157")
        val todayTotalB = BigDecimal("20.58")
        val arv = adv.除(acv, 18).let { if (it.大于(BigDecimal.ONE)) BigDecimal.ONE else it }.乘(todayTotalA)//A代币释放量
        val brv = adv.除(acv, 18).let { if (it.大于(BigDecimal.ONE)) BigDecimal.ONE else it }.乘(todayTotalB)//B代币释放量
        syncToRedis()
        redisTemplate.opsForValue().set(REDIS_KEY_TOKEN_A_TODAY_DESTROY, adv.number())
        redisTemplate.opsForValue().set(REDIS_KEY_TOKEN_B_TODAY_DESTROY, bdv.number())
        redisTemplate.opsForValue().set(REDIS_KEY_TOKEN_A_CIRCULATING, acv.number())
        redisTemplate.opsForValue().set(REDIS_KEY_TOKEN_B_CIRCULATING, bcv.number())
        val tokenAPrice = BigDecimal(
            LibWeb3.LIB_WEB_3.GetPancakeSwapTokenV2Price(//1U兑换token数量
                GoString.ByValue.of(properties.usdt), GoString.ByValue.of(properties.tokenA)
            )
        )
        val tokenBPrice = BigDecimal(
            LibWeb3.LIB_WEB_3.GetPancakeSwapTokenV2Price(//1U兑换token数量
                GoString.ByValue.of(properties.usdt), GoString.ByValue.of(properties.tokenB)
            )
        )
        val totalDynamicPower = getTotalDynamicPower()
        redisTemplate.opsForValue().set(REDIS_KEY_TOKEN_A_DYNAMIC_POWER, totalDynamicPower.number())
        val script = StringBuilder()
        logger.info(
            "A代币今日销毁量: {}, A代币全网流通量: {}, A代币释放量: {}, B代币释放量: {}, A代币价格: 1U={}, B代币价格: 1U={}, 总动态算力: {}",
            adv.number(), acv.number(), arv.number(), brv.number(), tokenAPrice.number(), tokenBPrice.number(), totalDynamicPower.number()
        )
        val sarv = arv.乘(BigDecimal("0.56")) //A代币释 静态 释放量
        val tokenANodeValues = getGiveNodeValue(arv.乘(BigDecimal("0.03"))) //3%分给小大超节点 小平分1% 大平分%1 超平分%1
        val aarv = arv.乘(BigDecimal("0.33")) //A代币释 动态 释放量
        val sbrv = brv.乘(BigDecimal("0.56")) //B代币释 静态 释放量
        val tokenBNodeValues = getGiveNodeValue(brv.乘(BigDecimal("0.03"))) //3%分给小大超节点 小平分1% 大平分%1 超平分%1  和卡牌无关
        val abrv = brv.乘(BigDecimal("0.33")) //B代币释 动态 释放量
        script.append("\r\n").append("UPDATE `wallet_card` SET `days`=0 WHERE `status`=1;")
        script.append("\r\n").append("-- 静态分红")
        staticRelease(sarv, sbrv, tokenAPrice, tokenBPrice, script)
        if (totalDynamicPower.大于(BigDecimal.ZERO)) {
            script.append("\r\n").append("-- 动态分红")
            dynamicRelease(aarv, abrv, totalDynamicPower, tokenAPrice, tokenBPrice, script)
        }
        script.append("\r\n").append("-- 节点分红")
        nodeValueRelease(tokenANodeValues, tokenBNodeValues, script)
        logger.info("script sql:\r\n{}", script.toString())
        jdbcTemplate.dataSource?.let { dataSource ->
            var connection: Connection? = null
            try {
                connection = DataSourceUtils.getConnection(dataSource)
                connection.autoCommit = false
                ScriptUtils.executeSqlScript(connection, InputStreamResource(ByteArrayInputStream(script.toString().toByteArray())))
                connection.commit()
            } catch (e: Exception) {
                connection?.rollback()
                logger.error("分红失败", e)
            } finally {
                connection?.autoCommit = true
                DataSourceUtils.releaseConnection(connection, dataSource)
            }
        }
        val carv = arv.乘(BigDecimal("0.05"))
        var hash = LibWeb3.LIB_WEB_3.SendERC20Transfer(
            GoString.ByValue.of(properties.walletPrivateKey),
            GoString.ByValue.of(properties.tokenA),
            GoString.ByValue.of(properties.communityAddress), //社区建设钱包
            GoString.ByValue.of(carv.number())
        )
        if(hash.isNullOrBlank()) {
            logger.error("【合约交易】Token A 社区建设钱包交易失败")
        } else {
            logger.info("【合约交易】Token A 社区建设钱包 hash: {}", hash)
        }
        val tarv = arv.乘(BigDecimal("0.03"))
        hash = LibWeb3.LIB_WEB_3.SendERC20Transfer(
            GoString.ByValue.of(properties.walletPrivateKey),
            GoString.ByValue.of(properties.tokenA),
            GoString.ByValue.of(properties.technologyAddress), //技术钱包
            GoString.ByValue.of(tarv.number())
        )
        if(hash.isNullOrBlank()) {
            logger.error("【合约交易】Token A 技术钱包交易失败")
        } else {
            logger.info("【合约交易】Token A 技术钱包 hash: {}", hash)
        }
        val sendTotalTokenA = walletService.getSumCheckAmountTotal(CheckInEntity.Type.TOKEN_A, LocalDate.now())
        val totalUseTokenA = sendTotalTokenA.加(carv).加(tarv)
        val destroyTokenA = todayTotalA.减(arv)
        //A代币销毁
        hash = LibWeb3.LIB_WEB_3.SendERC20Transfer(
            GoString.ByValue.of(properties.walletPrivateKey),
            GoString.ByValue.of(properties.tokenA),
            GoString.ByValue.of(BLACK_HOLE_ADDRESS_2), //黑洞地址
            GoString.ByValue.of(destroyTokenA.number())
        )
        if(hash.isNullOrBlank()) {
            logger.error("【合约交易】Token A 销毁失败交易失败")
        } else {
            logger.info("【合约交易】Token A 销毁 hash: {}", hash)
        }
        redisTemplate.opsForValue().set("send_token_a", sendTotalTokenA.number())
        logger.info("TokenA 今日总量：{}，释放量：{}，签到总量：{}，实际使用量：{}，实际销毁：{}", todayTotalA.number(), arv.number(), sendTotalTokenA.number(), totalUseTokenA.number(), destroyTokenA.number())

        val cbrv = brv.乘(BigDecimal("0.05"))
        hash = LibWeb3.LIB_WEB_3.SendERC20Transfer(
            GoString.ByValue.of(properties.walletPrivateKey),
            GoString.ByValue.of(properties.tokenB),
            GoString.ByValue.of(properties.communityAddress), //社区建设钱包
            GoString.ByValue.of(cbrv.number())
        )
        if(hash.isNullOrBlank()) {
            logger.error("【合约交易】社区建设钱包交易失败")
        } else {
            logger.info("【合约交易】社区建设钱包 hash: {}", hash)
        }
        val tbrv = brv.乘(BigDecimal("0.03"))
        hash = LibWeb3.LIB_WEB_3.SendERC20Transfer(
            GoString.ByValue.of(properties.walletPrivateKey),
            GoString.ByValue.of(properties.tokenB),
            GoString.ByValue.of(properties.technologyAddress), //技术钱包
            GoString.ByValue.of(tbrv.number())
        )
        if(hash.isNullOrBlank()) {
            logger.error("【合约交易】技术钱包交易失败")
        } else {
            logger.info("【合约交易】技术钱包 hash: {}", hash)
        }
        val sendTotalTokenB = walletService.getSumCheckAmountTotal(CheckInEntity.Type.TOKEN_B, LocalDate.now())
        val totalUseTokenB = sendTotalTokenB.加(cbrv).加(tbrv)
        val destroyTokenB = todayTotalB.减(brv)
        //B代币销毁
        hash = LibWeb3.LIB_WEB_3.SendERC20Transfer(
            GoString.ByValue.of(properties.walletPrivateKey),
            GoString.ByValue.of(properties.tokenB),
            GoString.ByValue.of(BLACK_HOLE_ADDRESS_2), //黑洞地址
            GoString.ByValue.of(destroyTokenB.number())
        )
        if(hash.isNullOrBlank()) {
            logger.error("【合约交易】销毁失败")
        } else {
            logger.info("【合约交易】销毁 hash: {}", hash)
        }
        redisTemplate.opsForValue().set("send_token_b", sendTotalTokenB.number())
        logger.info("TokenB 今日总量：{}，释放量：{}，签到总量：{}，实际使用量：{}，实际销毁：{}", todayTotalB.number(), brv.number(), sendTotalTokenB.number(), totalUseTokenB.number(), destroyTokenB.number())
    }

    private fun staticRelease(totalTokenA: BigDecimal, totalTokenB: BigDecimal, tokenAPrice: BigDecimal, tokenBPrice: BigDecimal, script: StringBuilder) {
        val totalAPower = walletService.currentTotalPower()
        redisTemplate.opsForValue().set(REDIS_KEY_TOKEN_A_STATIC_POWER, totalAPower.toString())
        val totalBPower = redisTemplate.opsForValue().get(REDIS_KEY_TOTAL_B_POWER) as? String ?: "0"
        logger.info("B 总算力：{}", totalBPower)
        if (totalAPower == 0L) return
        val oneTokenA = totalTokenA.除(BigDecimal.valueOf(totalAPower), 18).除(tokenAPrice, 18)
        val oneTokenB = if (totalBPower == "0") BigDecimal.ZERO else totalTokenB.除(BigDecimal(totalBPower), 18).除(tokenBPrice, 18)
        val redisSet = redisTemplate.opsForSet()

        val redisMap = redisTemplate.opsForHash<String, Any>()
        val cursor = redisTemplate.scan(ScanOptions.scanOptions().match("$REDIS_KEY_USER_CARD*").build())
        val date = DateTimeFormat.formatDate(LocalDate.now())
        while (cursor.hasNext()) {
            val key = cursor.next()
            val values = redisSet.members(key) as? Set<WalletCardEntity> ?: continue
            for (item in values) {
                val card = cards[item.cardModelId] ?: continue
                var status = item.status
                var num = BigDecimal(item.power).乘(oneTokenA)
                if (item.output.小于等于(num)) {
                    num = item.output
                    status = WalletCardEntity.Status.RELEASED
                }
                script.append("\r\n-- tokenA 分红")
                //修改用户卡牌产出减少和天数增加
                var sql = "UPDATE `wallet_card` SET `output`=`output`-'%s', `days`=`days`+1, `status`=%d WHERE `id`=%d;"
                script.append("\r\n").append(String.format(sql, num.number(), status.status, item.id))
                //增加A代币当天签到
                sql = "INSERT INTO `check_in`(`wallet_id`, `type`, `amount`, `status`, `date`) VALUE (%d, %d, '%s', %d, '%s');"
                script.append("\r\n").append(String.format(sql, item.walletId, 1, num.乘(tokenAPrice).number(), 0, date))
                //增加A代币分红记录
                sql = "INSERT INTO `asset_log`(`wallet_id`, `type`, `source`, `card_model_id`, `amount`) VALUE (%d, %d, %d, %d, '%s');"
                script.append("\r\n").append(String.format(sql, item.walletId, 2, 3, card.id, num.乘(tokenAPrice).number()))
                if (status == WalletCardEntity.Status.RELEASED) {
                    val suId = redisMap.get(item.walletId.REDIS_KEY_USER(), "superior_id") as? Int
                    if (suId != null) {
                        //减少上级直推算力
                        sql = "UPDATE `wallet` SET `zt_power`=IF(`zt_power`>=%d, (`zt_power`-%d), 0) WHERE `id`=%d;"
                        script.append("\r\n").append(String.format(sql, card.ztPower, card.ztPower, suId))
                    }
                }
                item.output = item.output.减(num)
                item.status = status

                if (oneTokenB.大于(BigDecimal.ZERO) && status == WalletCardEntity.Status.RELEASING) {
                    var bNum = WalletCardEntity.getPowerB(item).乘(oneTokenB)
                    if (bNum.大于(BigDecimal.ZERO)) {
                        if (item.output.小于等于(bNum)) {
                            bNum = item.output
                            status = WalletCardEntity.Status.RELEASED
                        }
                        script.append("\r\n").append("-- tokenB 分红")
                        //修改用户卡牌产出减少和天数增加
                        sql = "UPDATE `wallet_card` SET `output`=`output`-'%s', `status`=%d WHERE `id`=%d;"
                        script.append("\r\n").append(String.format(sql, bNum.number(), status.status, item.id))
                        //增加B代币当天签到
                        sql = "INSERT INTO `check_in`(`wallet_id`, `type`, `amount`, `status`, `date`) VALUE (%d, %d, '%s', %d, '%s');"
                        script.append("\r\n").append(String.format(sql, item.walletId, 2, bNum.乘(tokenBPrice).number(), 0, date))
                        //增加B代币分红记录
                        sql = "INSERT INTO `asset_log`(`wallet_id`, `type`, `source`, `card_model_id`, `amount`) VALUE (%d, %d, %d, %d, '%s');"
                        script.append("\r\n").append(String.format(sql, item.walletId, 3, 3, card.id, bNum.乘(tokenBPrice).number()))
                    }
                }
            }
            redisTemplate.delete(key)
            redisSet.add(key, *values.toTypedArray())
        }
    }

    private fun dynamicRelease(totalTokenA: BigDecimal, totalTokenB: BigDecimal, totalDynamicPower: BigDecimal, tokenAPrice: BigDecimal, tokenBPrice: BigDecimal, script: StringBuilder) {
        script.append("\r\n\r\n")
        val oneTokenA = totalTokenA.除(totalDynamicPower, 18).除(tokenAPrice, 18)
        val oneTokenB = totalTokenB.除(totalDynamicPower, 18).除(tokenBPrice, 18)
        val redisMap = redisTemplate.opsForHash<String, Any>()
        val cursor = redisTemplate.scan(ScanOptions.scanOptions().match("$REDIS_KEY_USER*").build())
        val date = DateTimeFormat.formatDate(LocalDate.now())
        while (cursor.hasNext()) {
            val key = cursor.next()
            val walletId = key.substring(5).toLong()
            val userKey = walletId.REDIS_KEY_USER()
            val dynamicPower = BigDecimal(redisMap.get(userKey, "dynamic_power") as? String ?: "0")
            val smallPower = redisMap.get(userKey, "small_power") as? Int ?: 0
            script.append("\r\n").append(String.format("REPLACE INTO `wallet_power`(`wallet_id`, `small_power`) VALUE (%d, %d);", walletId, smallPower))
            val yesterdaySmallPower = walletService.getYesterdaySmallPower(walletId)
            //动态算力增加释放锁仓A代币
            if (smallPower > yesterdaySmallPower) {
                val lockTokenA = BigDecimal(redisMap.get(userKey, "lock_token_a") as? String ?: "0")
                var relock = (smallPower - yesterdaySmallPower).toBigDecimal().乘(BigDecimal.valueOf(Node.SMALL_POWER_RATIO))
                if (relock.大于(lockTokenA)) {
                    relock = lockTokenA
                }
                script.append("\r\n").append(String.format("UPDATE `wallet` SET `lock_token_a`=`lock_token_a`-'%s' WHERE `id`=%d;", relock.number(), walletId))
                var sql = "INSERT INTO `check_in`(`wallet_id`, `type`, `amount`, `status`, `date`) VALUE (%d, %d, '%s', %d, '%s');"
                script.append("\r\n").append(String.format(sql, walletId, 1, relock.number(), 0, date))
                sql = "INSERT INTO `asset_log`(`wallet_id`, `type`, `source`, `amount`) VALUE (%d, %d, %d, '%s');"
                script.append("\r\n").append(String.format(sql, walletId, 2, 7, relock.number()))
            }
            val level = redisMap.get(userKey, "level") as? Int ?: 0
            if (level > 0 && dynamicPower.大于(BigDecimal.ZERO)) {
                var valueA = oneTokenA.乘(dynamicPower)
                var valueB = oneTokenB.乘(dynamicPower)
                val upperLimit = BigDecimal(levels[level.toLong()]!!.upperLimit)
                if (valueA.大于等于(upperLimit)) {
                    valueA = upperLimit
                    valueB = BigDecimal.ZERO
                } else {
                    val x = upperLimit.减(valueA)
                    if (valueB.大于(x)) {
                        valueB = x
                    }
                }
                val members = redisTemplate.opsForSet().members(walletId.REDIS_KEY_USER_CARD()) as? Set<WalletCardEntity>
                if (!members.isNullOrEmpty()) {
                    val sum = members.sumOf { cards[it.cardModelId]!!.output }
                    val valueAOne = valueA.除(BigDecimal(sum), 18)
                    val valueBOne = valueB.除(BigDecimal(sum), 18)
                    for (item in members) {
                        val card = cards[item.cardModelId] ?: continue
                        if (item.status == WalletCardEntity.Status.RELEASED) continue
                        var status = item.status
                        var numA = valueAOne.乘(BigDecimal(card.output))
                        if (item.output.小于等于(numA)) {
                            numA = item.output
                            status = WalletCardEntity.Status.RELEASED
                        }
                        script.append("\r\n").append("-- tokenA 分红")
                        //修改用户卡牌产出减少
                        var sql = "UPDATE `wallet_card` SET `status`=%d, `output`=`output`-'%s' WHERE `id`=%d;"
                        script.append("\r\n").append(String.format(sql, status.status, numA.number(), item.id))
                        //更新A代币当天签到数量
                        sql = "INSERT INTO `check_in`(`wallet_id`, `type`, `amount`, `status`, `date`) VALUE (%d, %d, '%s', %d, '%s');"
                        script.append("\r\n").append(String.format(sql, item.walletId, 1, numA.乘(tokenAPrice).number(), 0, date))
                        //增加A代币分红记录
                        sql = "INSERT INTO `asset_log`(`wallet_id`, `type`, `source`, `card_model_id`, `amount`) VALUE (%d, %d, %d, %d, '%s');"
                        script.append("\r\n").append(String.format(sql, item.walletId, 2, 4, card.id, numA.乘(tokenAPrice).number()))
                        if (status == WalletCardEntity.Status.RELEASED) {
                            val suId = redisMap.get(item.walletId.REDIS_KEY_USER(), "superior_id") as? Int
                            if (suId != null) {
                                //减少上级直推算力
                                sql = "UPDATE `wallet` SET `zt_power`=IF(`zt_power`>=%d, (`zt_power`-%d), 0) WHERE `id`=%d;"
                                script.append("\r\n").append(String.format(sql, card.ztPower, card.ztPower, suId))
                            }
                        }
                        item.status = status
                        item.output = item.output.减(numA)

                        var numB = valueBOne.乘(BigDecimal(card.output))
                        if (numB.大于(BigDecimal.ZERO)) {
                            if (item.output.小于等于(numB)) {
                                numB = item.output
                                status = WalletCardEntity.Status.RELEASED
                            }
                            script.append("\r\n").append("-- tokenB 分红")
                            //修改用户卡牌产出减少
                            sql = "UPDATE `wallet_card` SET `output`=`output`-'%s', `status`=%d WHERE `id`=%d;"
                            script.append("\r\n").append(String.format(sql, numB.number(), status.status, item.id))
                            //更新B代币当天签到数量
                            sql = "INSERT INTO `check_in`(`wallet_id`, `type`, `amount`, `status`, `date`) VALUE (%d, %d, '%s', %d, '%s');"
                            script.append("\r\n").append(String.format(sql, item.walletId, 2, numB.乘(tokenBPrice).number(), 0, date))
                            //增加B代币分红记录
                            sql = "INSERT INTO `asset_log`(`wallet_id`, `type`, `source`, `card_model_id`, `amount`) VALUE (%d, %d, %d, %d, '%s');"
                            script.append("\r\n").append(String.format(sql, item.walletId, 3, 4, card.id, numB.乘(tokenBPrice).number()))
                        }
                        item.status = status
                        item.output = item.output.减(numB)
                    }
                    redisTemplate.delete(walletId.REDIS_KEY_USER_CARD())
                    redisTemplate.opsForSet().add(walletId.REDIS_KEY_USER_CARD(), *members.toTypedArray())
                }
            }
        }
    }

    private fun nodeValueRelease(tokenANodeValues: Array<BigDecimal>, tokenBNodeValues: Array<BigDecimal>, script: StringBuilder) {
        val cursor = redisTemplate.scan(ScanOptions.scanOptions().match("$REDIS_KEY_USER*").build())
        val redisMap = redisTemplate.opsForHash<String, Any>()
        val date = DateTimeFormat.formatDate(LocalDate.now())
        val sql = "INSERT INTO `check_in`(`wallet_id`, `type`, `amount`, `status`, `date`) VALUE (%d, %d, '%s', %d, '%s');"
        val nodeSql = "INSERT INTO `asset_log`(`wallet_id`, `type`, `source`, `card_model_id`, `amount`) VALUE (%d, %d, %d, %d, '%s');"
        while (cursor.hasNext()) {
            val key = cursor.next()
            val walletId = key.substring(5).toLong()
            val userKey = walletId.REDIS_KEY_USER()
            when (Node.valueOfNode(redisMap.get(userKey, "node") as? Int ?: 0)) {
                Node.NODE_1 -> {
                    if (tokenANodeValues[0].大于(BigDecimal.ZERO)) {
                        script.append("\r\n").append(String.format(sql, walletId, 1, tokenANodeValues[0].number(), 0, date))
                        script.append("\r\n").append(String.format(nodeSql, walletId, 2, 9, 0, tokenANodeValues[0].number()))
                    }
                    if (tokenBNodeValues[0].大于(BigDecimal.ZERO)) {
                        script.append("\r\n").append(String.format(sql, walletId, 2, tokenBNodeValues[0].number(), 0, date))
                        script.append("\r\n").append(String.format(nodeSql, walletId, 3, 9, 0, tokenBNodeValues[0].number()))
                    }
                }

                Node.NODE_2 -> {
                    if (tokenANodeValues[1].大于(BigDecimal.ZERO)) {
                        script.append("\r\n").append(String.format(sql, walletId, 1, tokenANodeValues[1].number(), 0, date))
                        script.append("\r\n").append(String.format(nodeSql, walletId, 2, 9, 0, tokenANodeValues[1].number()))

                    }
                    if (tokenBNodeValues[1].大于(BigDecimal.ZERO)) {
                        script.append("\r\n").append(String.format(sql, walletId, 2, tokenBNodeValues[1].number(), 0, date))
                        script.append("\r\n").append(String.format(nodeSql, walletId, 3, 9, 0, tokenBNodeValues[1].number()))
                    }
                }

                Node.NODE_3 -> {
                    if (tokenANodeValues[2].大于(BigDecimal.ZERO)) {
                        script.append("\r\n").append(String.format(sql, walletId, 1, tokenANodeValues[2].number(), 0, date))
                        script.append("\r\n").append(String.format(nodeSql, walletId, 2, 9, 0, tokenANodeValues[2].number()))
                    }
                    if (tokenBNodeValues[2].大于(BigDecimal.ZERO)) {
                        script.append("\r\n").append(String.format(sql, walletId, 2, tokenBNodeValues[2].number(), 0, date))
                        script.append("\r\n").append(String.format(nodeSql, walletId, 3, 9, 0, tokenBNodeValues[2].number()))
                    }
                }

                else -> {}
            }
        }
    }

    private fun getTotalDynamicPower(): BigDecimal {
        val redisMap = redisTemplate.opsForHash<String, Any>()
        val cursor = redisTemplate.scan(ScanOptions.scanOptions().match("$REDIS_KEY_USER*").build())
        var totalA = BigDecimal.ZERO
        var totalB = BigDecimal.ZERO
        while (cursor.hasNext()) {
            val key = cursor.next()
            val walletId = key.substring(5).toLong()
            val smallPower = getSmallPower(walletId)
            val level = getLevelId(walletId, smallPower)
            val userKey = walletId.REDIS_KEY_USER()
            redisMap.put(userKey, "level", level)
            val ztPower = redisMap.get(userKey, "zt_power") as? Int ?: 0
            val bPower = BigDecimal(redisMap.get(userKey, "b_power") as? String ?: "0")
            val node = Node.valueOfNode(redisMap.get(userKey, "node") as? Int ?: 0)
            totalB = totalB.加(bPower)
            val power = if (level > 0) {
                BigDecimal(smallPower).乘(levels[level]!!.weight).also {
                    redisMap.put(userKey, "team_dynamic_power", it.number())
                }.加(BigDecimal(ztPower)).also {
                    redisMap.put(userKey, "node_power", node.getWeightPower(it).number())
                }.let { node.getPower(it) }
            } else {
                BigDecimal(ztPower).also {
                    redisMap.put(userKey, "node_power", node.getWeightPower(it).number())
                }.let { node.getPower(it) }
            }
            totalA = totalA.加(power)
            logger.info("{} 小区算力：{}，直推算力：{}，等级：{}，动态算力：{}", walletId, smallPower, ztPower, level, power.number())
            redisMap.put(userKey, "dynamic_power", power.number())
        }
        redisTemplate.opsForValue().set(REDIS_KEY_TOTAL_B_POWER, totalB.number())
        return totalA
    }

    private fun syncToRedis() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushDb(RedisServerCommands.FlushOption.SYNC)
        val redisMap = redisTemplate.opsForHash<String, Any>()
        val redisSet = redisTemplate.opsForSet()

        var pageSize = 1000
        var page = 0
        var pageWallet: Page<WalletEntity>?
        do {
            pageWallet = walletService.listWallet(page, pageSize)
            val list = pageWallet.content
            for (entity in list) {
                syncWalletToRedis(entity)
                logger.info("{} 静态算力：{}，直推算力：{}", entity.id, redisMap.get(entity.id.REDIS_KEY_USER(), "power"), entity.ztPower)
            }
            page++
        } while (pageWallet != null && pageWallet.totalPages > page)

        pageSize = 1000
        page = 0
        var pageWalletCard: Page<WalletCardEntity>?
        do {
            pageWalletCard = walletService.listWalletCard(page, pageSize)
            val list = pageWalletCard.content
            for (entity in list) {
                redisSet.add(entity.walletId.REDIS_KEY_USER_CARD(), entity)
                val userKey = entity.walletId.REDIS_KEY_USER()
                val bPower = redisMap.get(userKey, "b_power") as? String ?: "0"
                redisMap.put(userKey, "b_power", WalletCardEntity.getPowerB(entity).加(BigDecimal(bPower)).number())
            }
            page++
        } while (pageWalletCard != null && pageWalletCard.totalPages > page)

        pageSize = 1000
        page = 0
        var pageRelation: Page<RelationEntity>?
        do {
            pageRelation = walletService.listRelation(page, pageSize)
            val list = pageRelation.content
            for (entity in list) {
                redisMap.put(entity.walletId.REDIS_KEY_USER(), "superior_id", entity.superiorId)
                redisSet.add(entity.superiorId.REDIS_KEY_USER_CHILD_1(), entity.walletId)
                val ids = entity.relationPath.split(",").map { it.toLong() }.toLongArray()
                for (id in ids) {
                    redisSet.add(id.REDIS_KEY_USER_CHILD_ALL(), entity.walletId)
                }
            }
            page++
        } while (pageRelation != null && pageRelation.totalPages > page)
    }

    private fun getSmallPower(walletId: Long): Long {
        val redisMap = redisTemplate.opsForHash<String, Any>()
        val key = REDIS_KEY_USER + walletId

        val members = redisTemplate.opsForSet().members(REDIS_KEY_USER_CHILD_1 + walletId)
        if (members.isNullOrEmpty()) {
            redisMap.put(key, "big_power", 0)
            redisMap.put(key, "small_power", 0)
            return 0
        }
        val list = members.map { getSumPower((it as Int).toLong()) }.toMutableList()
        list.sortBy { it }
        val bigPower = list.removeLast()
        val smallPower = list.sum()
        redisMap.put(key, "big_power", bigPower)
        redisMap.put(key, "small_power", smallPower)
        return smallPower
    }

    private fun getSumPower(walletId: Long): Long {
        val sum = redisTemplate.opsForHash<String, Any>().get(REDIS_KEY_USER + walletId, "power") as? Int ?: 0
        return getAllChildPower(walletId) + sum
    }

    private fun getAllChildPower(walletId: Long): Long {
        val redisMap = redisTemplate.opsForHash<String, Any>()
        val key = REDIS_KEY_USER + walletId
        var childPower = redisMap.get(key, "child_power") as? Int
        if (childPower != null) return childPower.toLong()
        val members = redisTemplate.opsForSet().members(REDIS_KEY_USER_CHILD_ALL + walletId)
        if (members.isNullOrEmpty()) {
            redisMap.put(key, "child_power", 0)
            redisMap.put(key, "child_buy_node_sum", "0")
            return 0
        }
        childPower = 0
        var buySum = 0L
        for (member in members) {
            childPower += redisMap.get(REDIS_KEY_USER + member, "power") as? Int ?: 0
            buySum += redisMap.get(REDIS_KEY_USER + member, "buy_node_sum") as? Int ?: 0
        }
        redisMap.put(key, "child_power", childPower)
        redisMap.put(key, "child_buy_node_sum", buySum)
        return childPower.toLong()
    }

    private fun getLevelId(walletId: Long, smallPower: Long): Long {
        val smallPowerLevel = when {
            smallPower >= levels[5]!!.smallPower -> levels[5]!!.id
            smallPower >= levels[4]!!.smallPower -> levels[4]!!.id
            smallPower >= levels[3]!!.smallPower -> levels[3]!!.id
            smallPower >= levels[2]!!.smallPower -> levels[2]!!.id
            smallPower >= levels[1]!!.smallPower -> levels[1]!!.id
            else -> 0L
        }
        val members = redisTemplate.opsForSet().members(walletId.REDIS_KEY_USER_CARD()) ?: return 0
        var currentLevel = 0L
        for (member in members) {
            val card = member as WalletCardEntity
            if (currentLevel < card.cardModelId) {
                currentLevel = card.cardModelId
            }
        }
        return min(currentLevel, smallPowerLevel)
    }

    private fun getCirculationValue(token: String): BigDecimal {
        if (properties.tokenA.equals(token, true)) {
            val totalSupply = BigDecimal("210000000")
            val a0 = BigDecimal(
                LibWeb3.LIB_WEB_3.GetERC20Balance(
                    GoString.ByValue.of(properties.tokenA),
                    GoString.ByValue.of(BLACK_HOLE_ADDRESS_1)
                )
            )
            val a1 = BigDecimal(
                LibWeb3.LIB_WEB_3.GetERC20Balance(
                    GoString.ByValue.of(properties.tokenA),
                    GoString.ByValue.of(BLACK_HOLE_ADDRESS_2)
                )
            )
            //每日分红合约
            val rac = BigDecimal(
                LibWeb3.LIB_WEB_3.GetERC20Balance(
                    GoString.ByValue.of(properties.tokenA),
                    GoString.ByValue.of(properties.releaseContract)
                )
            )
            //认购合约
            val rgc = BigDecimal(
                LibWeb3.LIB_WEB_3.GetERC20Balance(
                    GoString.ByValue.of(properties.tokenA),
                    GoString.ByValue.of(properties.rengGouContract)
                )
            )
            //用户钱包每日分红的钱包
            val uaddr = BigDecimal(
                LibWeb3.LIB_WEB_3.GetERC20Balance(
                    GoString.ByValue.of(properties.tokenA),
                    GoString.ByValue.of(properties.walletPrivateKey)
                )
            )
            return totalSupply.减(a0).减(a1).减(rac).减(rgc).减(uaddr)
        } else {
            val totalSupply = BigDecimal("21000")
            val a0 = BigDecimal(
                LibWeb3.LIB_WEB_3.GetERC20Balance(
                    GoString.ByValue.of(properties.tokenB),
                    GoString.ByValue.of(BLACK_HOLE_ADDRESS_1)
                )
            )
            //合约
            val rbc = BigDecimal(
                LibWeb3.LIB_WEB_3.GetERC20Balance(
                    GoString.ByValue.of(properties.tokenB),
                    GoString.ByValue.of(properties.releaseContract)
                )
            )
            return totalSupply.减(a0).减(rbc)
        }
    }

    private fun getTodayDestroyValue(token: String): BigDecimal {
        var td = tokenDestroyService.findByTokenAndDay(token, LocalDate.now())
        if (td != null) {
            return td.currentAmount.减(td.lastAmount)
        }
        val lastDay = LocalDate.now().minusDays(1)
        var lastAmount = BigDecimal.ZERO
        td = tokenDestroyService.findByTokenAndDay(token, lastDay)
        if (td != null) {
            lastAmount = td.currentAmount
        }
        val balance = BigDecimal(
            LibWeb3.LIB_WEB_3.GetERC20Balance(
                GoString.ByValue.of(token),
                GoString.ByValue.of(BLACK_HOLE_ADDRESS_1)
            )
        )
        td = TokenDestroyEntity().also {
            it.day = LocalDate.now()
            it.token = token
            it.lastAmount = lastAmount
            it.currentAmount = balance
        }
        tokenDestroyService.save(td)
        return balance.减(lastAmount)
    }

    private fun syncWalletToRedis(wallet: WalletEntity? = null, walletId: Long? = null) {
        var entity: WalletEntity? = null
        if (wallet != null) {
            entity = wallet
        } else if (walletId != null) {
            entity = walletService.findWalletById(walletId)
        }
        if (entity == null) return;
        val redisMap = redisTemplate.opsForHash<String, Any>()
        val key = entity.id.REDIS_KEY_USER()
        redisMap.put(key, "wallet", entity.wallet)
        redisMap.put(key, "zt_power", entity.ztPower)
        redisMap.put(key, "power", walletService.walletTotalPower(entity.id))
        redisMap.put(key, "node", entity.node.node)
        redisMap.put(key, "lock_token_a", entity.lockTokenA.number())
        redisMap.put(key, "buy_node_sum", buyNodeOrderService.getWalletBuyNodeSumPrice(entity.id))
    }

    private fun syncWalletCardToRedis(walletId: Long) {
        val redisMap = redisTemplate.opsForHash<String, Any>()
        val redisSet = redisTemplate.opsForSet()
        val list = walletService.listByWalletCard(walletId)
        for (entity in list) {
            redisSet.add(entity.walletId.REDIS_KEY_USER_CARD(), entity)
            val userKey = entity.walletId.REDIS_KEY_USER()
            val bPower = redisMap.get(userKey, "b_power") as? String ?: "0"
            redisMap.put(userKey, "b_power", WalletCardEntity.getPowerB(entity).加(BigDecimal(bPower)).number())
        }
    }

    private fun getGiveNodeValue(total: BigDecimal): Array<BigDecimal> {
        val a = total.除(BigDecimal("3"), 18)
        val countNode1 = walletService.countByNode(Node.NODE_1)
        val countNode2 = walletService.countByNode(Node.NODE_2)
        val countNode3 = walletService.countByNode(Node.NODE_3)
        val a1 = if (countNode1 > 0) a.除(BigDecimal(countNode1), 18) else BigDecimal.ZERO
        val a2 = if (countNode2 > 0) a.除(BigDecimal(countNode2), 18) else BigDecimal.ZERO
        val a3 = if (countNode3 > 0) a.除(BigDecimal(countNode3), 18) else BigDecimal.ZERO
        return arrayOf(a1, a2, a3)
    }

    @Async
    fun updatePrice() {
        try {
            Node.values().forEach {
                if (it.node > 0) {
                    val json = LibWeb3.LIB_WEB_3.GetByNode(GoString.ByValue.of(properties.rengGouContract), it.node.toLong())
                    val obj = JSON.parseObject(json)
                    if (obj != null) {
                        val price = obj.getInteger("price")
                        if (price != null && price > 0) {
                            it.price = price
                        }
                        val period = obj.getInteger("period")
                        if (period != null && period > 0) {
                            it.period = period
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val tokenAPrice = BigDecimal(
                LibWeb3.LIB_WEB_3.GetPancakeSwapTokenV2Price(//1U兑换token数量
                    GoString.ByValue.of(properties.usdt), GoString.ByValue.of(properties.tokenA)
                )
            )
            TOKEN_A_PRICE = tokenAPrice
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val tokenAPrice = BigDecimal(
                LibWeb3.LIB_WEB_3.GetPancakeSwapTokenV2Price(//1U兑换token数量
                    GoString.ByValue.of(properties.usdt), GoString.ByValue.of(properties.tokenB)
                )
            )
            TOKEN_B_PRICE = tokenAPrice
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}