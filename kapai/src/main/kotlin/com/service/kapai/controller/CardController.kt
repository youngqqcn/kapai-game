package com.service.kapai.controller

import com.service.boot.i18n.I18nMessage
import com.service.boot.json.JSON
import com.service.boot.json.JSONObject
import com.service.boot.model.ApiResponse
import com.service.kapai.ApiRequestExamples
import com.service.kapai.ApiResponseExamples
import com.service.kapai.REDIS_KEY_ORDER
import com.service.kapai.configuration.AppProperties
import com.service.kapai.jna.LibWeb3
import com.service.kapai.repository.entity.MoldOrderEntity
import com.service.kapai.repository.model.TransactionStatus
import com.service.kapai.service.CardModelService
import com.service.kapai.service.MoldOrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "卡牌游戏")
@RestController
@RequestMapping("card")
class CardController(
    private val i18nMessage: I18nMessage,
    private val properties: AppProperties,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val cardModelService: CardModelService,
    private val moldOrderService: MoldOrderService
) : BaseController() {

    @Operation(
        summary = "卡牌列表",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.card_list)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @GetMapping("list")
    fun list(): ApiResponse {
        return ApiResponse.success(cardModelService.findAll())
    }

    @Operation(
        summary = "获取铸造卡牌签名数据",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.card_mold_data)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @PostMapping("mold/data")
    fun getMoldData(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = [Content(examples = [ExampleObject(value = ApiRequestExamples.card_mold_data)], mediaType = MediaType.APPLICATION_JSON_VALUE)])
        @RequestBody body: JSONObject
    ): ApiResponse {
        val cardId = body.getLong("cardId") ?: return ApiResponse.error(i18nMessage.getMessage("request.params.error.p1", "cardId"))
        if (cardId !in 1..5) return ApiResponse.error(i18nMessage.getMessage("card_does_not_exist"))
        val card = cardModelService.findById(cardId) ?: return ApiResponse.error(i18nMessage.getMessage("card_does_not_exist"))
        val type = body.getInteger("type") ?: 0
        val useEP = type == 1
        val order = MoldOrderEntity().also {
            it.walletId = currentUser().id
            it.cardModelId = card.id
            it.type = type
            it.cardData = JSON.toJSONString(card)
        }
        moldOrderService.saveOrUpdateMoldOrder(order, null, null)
        val inputData = LibWeb3.LIB_WEB_3.GetMoldEncodeData(cardId.toInt(), order.id, useEP)
        return ApiResponse.success(
            mapOf(
                "orderId" to order.id,
                "chainId" to properties.chainId,
                "wallet" to currentUser().wallet,
                "contractTokenA" to properties.tokenA,
                "contractEP" to properties.tokenEP,
                "data" to inputData
            )
        )
    }

    @Operation(
        summary = "发送铸造交易",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.card_mold_send)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @PostMapping("mold/send")
    fun send(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = [Content(examples = [ExampleObject(value = ApiRequestExamples.card_mold_send)], mediaType = MediaType.APPLICATION_JSON_VALUE)])
        @RequestBody body: JSONObject
    ): ApiResponse {
        val orderId = body.getLong("orderId") ?: return ApiResponse.error(i18nMessage.getMessage("request.params.error.p1", "orderId"))
        val hash = body.getString("hash") ?: return ApiResponse.error(i18nMessage.getMessage("request.params.error.p1", "hash"))
        val order = moldOrderService.findMoldOrderById(orderId) ?: return ApiResponse.error(i18nMessage.getMessage("transaction_does_not_exist"))
        if (order.walletId != currentUser().id) return ApiResponse.error(i18nMessage.getMessage("transaction_does_not_exist"))
        if (order.status != TransactionStatus.CREATED) return ApiResponse.error(i18nMessage.getMessage("transaction_processed"))
        if (hash.equals(order.txHash, true)) return ApiResponse.success(i18nMessage.getMessage("transaction_duplication"))
        order.status = TransactionStatus.IN_PROGRESS
        order.txHash = hash
        moldOrderService.saveOrUpdateMoldOrder(order, null, null)
        redisTemplate.opsForList().rightPush(REDIS_KEY_ORDER, order)
        return ApiResponse.success()
    }
}