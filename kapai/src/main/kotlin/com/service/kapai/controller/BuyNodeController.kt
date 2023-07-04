package com.service.kapai.controller

import com.service.kapai.ApiRequestExamples
import com.service.kapai.ApiResponseExamples
import com.service.kapai.REDIS_KEY_ORDER
import com.service.kapai.configuration.AppProperties
import com.service.kapai.jna.LibWeb3
import com.service.kapai.jna.go.GoString
import com.service.kapai.repository.entity.BuyNodeOrderEntity
import com.service.kapai.repository.model.Node
import com.service.kapai.repository.model.TransactionStatus
import com.service.kapai.service.BuyNodeOrderService
import com.service.kapai.service.WalletService
import com.service.boot.common.DateTimeFormat
import com.service.boot.i18n.I18nMessage
import com.service.boot.json.JSONObject
import com.service.boot.model.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "节点相关")
@RestController
@RequestMapping("node")
class BuyNodeController(
    private val i18nMessage: I18nMessage,
    private val properties: AppProperties,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val walletService: WalletService,
    private val buyNodeOrderService: BuyNodeOrderService
) : BaseController() {

    @Operation(
        summary = "节点列表",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.node_list)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @GetMapping("list")
    fun list(): ApiResponse {
        return ApiResponse.success(
            listOf(
                Node.NODE_1.getInfo(properties.rengGouContract),
                Node.NODE_2.getInfo(properties.rengGouContract),
                Node.NODE_3.getInfo(properties.rengGouContract)
            )
        )
    }

    @Operation(
        summary = "获取购买节点签名数据",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.node_buy_data)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @PostMapping("buy")
    fun buyNode(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = [Content(examples = [ExampleObject(value = ApiRequestExamples.node_buy_node)], mediaType = MediaType.APPLICATION_JSON_VALUE)])
        @RequestBody body: JSONObject
    ): ApiResponse {
        val n = body.getInteger("node") ?: return ApiResponse.error(i18nMessage.getMessage("request.params.error"))
        val node = Node.valueOfNode(n)
        if (node == Node.NODE_0) return ApiResponse.error(i18nMessage.getMessage("request.params.error"))
        val superiorId = walletService.findRelation(currentUser().id)?.superiorId ?: return ApiResponse.error(i18nMessage.getMessage("recommender_does_not_exist"))
        val recommender = walletService.findWalletById(superiorId) ?: return ApiResponse.error(i18nMessage.getMessage("recommender_does_not_exist"))
        val order = BuyNodeOrderEntity().also {
            it.walletId = currentUser().id
            it.node = node
        }
        buyNodeOrderService.saveOrUpdateNodeOrder(order, null, null)
        val inputData = LibWeb3.LIB_WEB_3.GetBuyNodeEncodeData(node.node.toLong(), GoString.ByValue.of(recommender.wallet), order.id)
        return ApiResponse.success(
            mapOf(
                "orderId" to order.id,
                "chainId" to properties.chainId,
                "wallet" to currentUser().wallet,
                "contract" to properties.rengGouContract,
                "usdt" to properties.usdt,
                "data" to inputData
            )
        )
    }

    @Operation(
        summary = "提交购买交易hash",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.default)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @PostMapping("send")
    fun send(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = [Content(examples = [ExampleObject(value = ApiRequestExamples.node_send)], mediaType = MediaType.APPLICATION_JSON_VALUE)])
        @RequestBody body: JSONObject
    ): ApiResponse {
        val orderId = body.getLong("orderId") ?: return ApiResponse.error(i18nMessage.getMessage("request.params.error.p1", "orderId"))
        val hash = body.getString("hash") ?: return ApiResponse.error(i18nMessage.getMessage("request.params.error.p1", "hash"))
        val order = buyNodeOrderService.findBuyNodeOrder(orderId) ?: return ApiResponse.error(i18nMessage.getMessage("transaction_does_not_exist"))
        if (order.walletId != currentUser().id) return ApiResponse.error(i18nMessage.getMessage("transaction_does_not_exist"))
        if (order.status != TransactionStatus.CREATED) return ApiResponse.error(i18nMessage.getMessage("transaction_processed"))
        if (hash.equals(order.txHash, true)) return ApiResponse.success(i18nMessage.getMessage("transaction_duplication"))
        order.status = TransactionStatus.IN_PROGRESS
        order.txHash = hash
        buyNodeOrderService.saveOrUpdateNodeOrder(order, null, null)
        redisTemplate.opsForList().rightPush(REDIS_KEY_ORDER, order)
        return ApiResponse.success()
    }

    @Operation(
        summary = "订单列表",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(content = [Content(examples = [ExampleObject(value = ApiResponseExamples.node_order_list)], mediaType = MediaType.APPLICATION_JSON_VALUE)])]
    )
    @GetMapping("orders")
    fun order(@Parameter(`in` = ParameterIn.QUERY, description = "页码 从0开始") page: Int): ApiResponse{
        return ApiResponse.success(buyNodeOrderService.getOrders(currentUser().id, 20, page).map { order ->
            order.node.getInfo(properties.rengGouContract).also { map ->
                map.put("status", order.status.status)
                map.put("txHash", order.txHash)
                map.put("period", order.period)
                map.put("price", order.price)
                map.put("ep", order.ep)
                map.put("createTime", DateTimeFormat.formatDateTime(order.createTime))
            }
        })
    }

}