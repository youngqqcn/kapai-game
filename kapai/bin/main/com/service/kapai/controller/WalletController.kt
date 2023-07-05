package com.service.kapai.controller

import com.service.kapai.*
import com.service.kapai.repository.entity.CheckInEntity
import com.service.kapai.service.WalletService
import com.service.boot.common.NumberUtils.小于等于
import com.service.boot.common.TransferLock
import com.service.boot.exception.AppException
import com.service.boot.i18n.I18nMessage
import com.service.boot.json.JSONObject
import com.service.boot.model.ApiResponse
import com.service.boot.number
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Tag(name = "钱包相关")
@RestController
@RequestMapping("wallet")
class WalletController(
    private val i18nMessage: I18nMessage,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val walletService: WalletService
) : BaseController() {

    @Operation(
        summary = "个人信息",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.wallet_info)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @GetMapping("info")
    fun info(): ApiResponse {
        val wallet = walletService.findWalletById(currentUser().id) ?: return ApiResponse.error()
        val superior = walletService.findSuperior(currentUser().id)
        val checkTokenA = walletService.getSumAmount(currentUser().id, CheckInEntity.Type.TOKEN_A, CheckInEntity.Status.IN_PROGRESS, LocalDate.now())
        val checkTokenB = walletService.getSumAmount(currentUser().id, CheckInEntity.Type.TOKEN_B, CheckInEntity.Status.IN_PROGRESS, LocalDate.now())
        val map = redisTemplate.opsForHash<String, Any>()
        val userKey = currentUser().id.REDIS_KEY_USER()
//        map.entries(userKey)
        val power = map.get(userKey, "power") as? Int ?: 0//A代币静态算力
        val bPower = map.get(userKey, "b_power") as? String ?: "0"//B代币静态算力
        val smallPower = map.get(userKey, "small_power") as? Int ?: 0//小区算力
        val bigPower = map.get(userKey, "big_power") as? Int ?: 0//大区算力
        val level = map.get(userKey, "level") as? Int ?: 0//团队等级
        val dynamicPower = map.get(userKey, "dynamic_power") as? String ?: "0"//动态算力
        val teamDynamicPower = map.get(userKey, "team_dynamic_power") as? String ?: "0"//团队算力
        val nodePower = map.get(userKey, "node_power") as? String ?: "0"//节点算力
        return ApiResponse.success(
            mapOf(
                "wallet" to wallet.wallet,
                "superior" to (superior?.wallet ?: ""),
                "ztPower" to wallet.ztPower,
                "tokenA" to checkTokenA.number(),
                "tokenB" to checkTokenB.number(),
                "lockTokenA" to wallet.lockTokenA.number(),
                "power" to power,
                "bPower" to bPower,
                "smallPower" to smallPower,
                "bigPower" to bigPower,
                "level" to level,
                "dynamicPower" to if (level > 0) dynamicPower else "0",
                "teamDynamicPower" to teamDynamicPower,
                "nodePower" to nodePower,
                "nodeLevel" to wallet.node,
            )
        )
    }

    @Operation(
        summary = "我的粉丝",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.wallet_fans)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @GetMapping("fans")
    fun fans(): ApiResponse {
        val members = redisTemplate.opsForSet().members(currentUser().id.REDIS_KEY_USER_CHILD_1()) as? Set<Int>
        if (members.isNullOrEmpty()) {
            return ApiResponse.success(emptyList<Any>())
        }
        val wallets = walletService.findWalletById(members.map { it.toLong() })
        val map = redisTemplate.opsForHash<String, Any>()
        val list = mutableListOf<Any>()
        for (wallet in wallets) {
            val key = wallet.id.REDIS_KEY_USER()
            val power = map.get(key, "power") as? Int ?: 0
            val childPower = map.get(key, "child_power") as? Int ?: 0
            list.add(
                mapOf(
                    "wallet" to wallet.wallet,
                    "power" to (power + childPower)
                )
            )
        }
        return ApiResponse.success(list)
    }

    @Operation(
        summary = "绑定推荐人",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.default)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @PostMapping("bind")
    fun bindRelation(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = [Content(examples = [ExampleObject(value = ApiRequestExamples.wallet_bind)], mediaType = MediaType.APPLICATION_JSON_VALUE)])
        @RequestBody body: JSONObject
    ): ApiResponse {
        val wallet = body.getString("wallet")?.lowercase() ?: return ApiResponse.error("参数错误")
        if (wallet.equals(currentUser().wallet, true)) return ApiResponse.error("不能绑定自己")
        return try {
            walletService.bindRelation(currentUser().id, wallet)
            currentUser().isBind = true
            ApiResponse.success()
        } catch (e: AppException) {
            ApiResponse.error(e.code, e.message)
        }
    }

    @Operation(
        summary = "获取我的卡牌数据",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.wallet_cards)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @GetMapping("cards")
    fun myCardInfo(): ApiResponse {
        return ApiResponse.success(walletService.getMyCards(currentUser().id))
    }

    @Operation(
        summary = "相关日志接口",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.wallet_logs)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @GetMapping("logs")
    fun logs(type: Array<Int>, source: Array<Int>, linkId: Long?, beforeDate: LocalDate?, page: Int): ApiResponse {
        return ApiResponse.success(walletService.logs(type, source, currentUser().id, linkId, beforeDate, page))
    }

    @Operation(
        summary = "签到",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.default)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @GetMapping("checkIn")
    fun checkIn(@Parameter(`in` = ParameterIn.QUERY, description = "类型：1:A代币 2:B代币") type: Int): ApiResponse {
        val tokenType = when (type) {
            CheckInEntity.Type.TOKEN_A.type -> {
                CheckInEntity.Type.TOKEN_A
            }

            CheckInEntity.Type.TOKEN_B.type -> {
                CheckInEntity.Type.TOKEN_B
            }

            else -> CheckInEntity.Type.UNKNOWN
        }
        if (tokenType == CheckInEntity.Type.UNKNOWN) return ApiResponse.error(i18nMessage.getMessage("request.params.error"))
        val wallet = walletService.findWalletById(currentUser().id) ?: return ApiResponse.error()
        return TransferLock.tryTransfer(currentUser().wallet) {
            if (it) {
                val value = walletService.getSumAmount(currentUser().id, tokenType, CheckInEntity.Status.IN_PROGRESS, LocalDate.now())
                if (value.小于等于(BigDecimal.ZERO)) {
                    return@tryTransfer ApiResponse.error(i18nMessage.getMessage("insufficient_quantity"))
                }
                if (walletService.checkIn(tokenType, wallet, value)) {
                    return@tryTransfer ApiResponse.success()
                }
                return@tryTransfer ApiResponse.error(i18nMessage.getMessage("withdrawal_failed"))
            } else {
                return@tryTransfer ApiResponse.error(i18nMessage.getMessage("processing"))
            }
        }!!
    }
}