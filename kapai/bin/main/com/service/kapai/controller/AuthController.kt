package com.service.kapai.controller

import com.service.kapai.*
import com.service.kapai.configuration.AppProperties
import com.service.kapai.jna.LibWeb3
import com.service.kapai.jna.go.GoString
import com.service.kapai.repository.entity.AssetLogEntity
import com.service.kapai.repository.entity.SwapEPOrderEntity
import com.service.kapai.service.WalletService
import com.service.kapai.task.Tasks
import com.service.kapai.utils.Ed25519Utils
import com.service.boot.common.NumberUtils
import com.service.boot.common.NumberUtils.加
import com.service.boot.common.NumberUtils.大于
import com.service.boot.common.NumberUtils.小于等于
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
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import net.i2p.crypto.eddsa.EdDSAEngine
import net.i2p.crypto.eddsa.EdDSAPublicKey
import net.i2p.crypto.eddsa.Utils
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.security.spec.X509EncodedKeySpec
import java.util.*
import kotlin.math.abs

@Tag(name = "登录认证")
@RestController
class AuthController(
    private val i18nMessage: I18nMessage,
    private val properties: AppProperties,
    private val walletService: WalletService,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val tasks: Tasks,
) : BaseController() {

    @Operation(
        summary = "获取签名信息",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(
            content = [Content(
                examples = [ExampleObject(
                    value = ApiResponseExamples.auth_signMessage
                )], mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        )]
    )
    @GetMapping("/auth/signMessage")
    fun getSignMessage(
        session: HttpSession,
        @Parameter(`in` = ParameterIn.QUERY, description = "钱包地址")
        wallet: String
    ): ApiResponse {
        val signMessage = "login:${session.id}"
        session.setAttribute("sign-wallet", wallet.lowercase())
        return ApiResponse.success(signMessage)
    }

    @Operation(
        summary = "获取代币价格",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(
            content = [Content(
                examples = [ExampleObject(
                    value = ApiResponseExamples.token_price
                )], mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        )]
    )
    @GetMapping("/token/price")
    fun tokenPrice(): ApiResponse {
        return ApiResponse.success(
            mapOf(
                "tokenA" to TOKEN_A_PRICE.number(),
                "tokenB" to TOKEN_B_PRICE.number(),
                "epRatio" to properties.epRatio
            )
        )
    }

    @Operation(
        summary = "数据查询",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(
            content = [Content(
                examples = [ExampleObject(
                    value = ApiResponseExamples.power_info
                )], mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        )]
    )
    @GetMapping("/power/info")
    fun fullPowerInfo(): ApiResponse {
        val map = mutableMapOf<String, Any>()
        map["aTodayDestroy"] = redisTemplate.opsForValue().get(REDIS_KEY_TOKEN_A_TODAY_DESTROY) ?: "0"
        map["bTodayDestroy"] = redisTemplate.opsForValue().get(REDIS_KEY_TOKEN_B_TODAY_DESTROY) ?: "0"
        map["aCirculating"] = redisTemplate.opsForValue().get(REDIS_KEY_TOKEN_A_CIRCULATING) ?: "0"
        map["bCirculating"] = redisTemplate.opsForValue().get(REDIS_KEY_TOKEN_B_CIRCULATING) ?: "0"
        map["aStaticPower"] = redisTemplate.opsForValue().get(REDIS_KEY_TOKEN_A_STATIC_POWER) ?: "0"
        map["bStaticPower"] = redisTemplate.opsForValue().get(REDIS_KEY_TOTAL_B_POWER) ?: "0"
        map["aDynamicPower"] = redisTemplate.opsForValue().get(REDIS_KEY_TOKEN_A_DYNAMIC_POWER) ?: "0"
        map["bDynamicPower"] = map["aDynamicPower"] ?: "0"
        return ApiResponse.success(map)
    }

    @Operation(
        summary = "钱包登录",
        responses = [io.swagger.v3.oas.annotations.responses.ApiResponse(
            content = [Content(
                examples = [ExampleObject(
                    value = ApiResponseExamples.api_auth_sign
                )], mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        )]
    )
    @PostMapping("/api/auth/sign")
    fun sign(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [Content(
                examples = [ExampleObject(value = ApiRequestExamples.api_auth_sign)],
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        )
        @RequestBody
        body: JSONObject
    ): ApiResponse {
        return ApiResponse.success()
    }

    @Operation(summary = "EP兑换")
    @PostMapping("/admin/swap/ep")
    fun swapEP(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [Content(
                examples = [ExampleObject(value = ApiRequestExamples.admin_swap_ep)],
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        )
        @RequestBody body: JSONObject
    ): ApiResponse {
        val sign = body.getString("sign") ?: return ApiResponse.error(400, "sign 参数错误")
        val timestamp = body.getIntegerValue("timestamp")
        if(abs(System.currentTimeMillis() / 1000  - timestamp) > 10) {
            return ApiResponse.error(405, "timestamp已过期")
        }
        val orderId = body.getString("orderId") ?: return ApiResponse.error(400, "orderId 参数不正确")
        val wallet = body.getString("wallet") ?: return ApiResponse.error(400, "wallet 参数不正确")
        val amountStr = body.getString("amount") ?: "0"
        val amount = BigDecimal(amountStr)
        if (amount.小于等于(BigDecimal.ZERO)) {
            return ApiResponse.error(400, "数量不正确")
        }
        val origin = "$orderId$wallet$amountStr$timestamp".lowercase()
        try {
            if(!Ed25519Utils.Ed25519VerifySig(properties.swapPublicKey, origin, sign)) {
                return ApiResponse.error(401, "签名错误")
            }
        } catch (e: Exception){
            return ApiResponse.error(401, "签名错误")
        }
        val we = walletService.findWallet(wallet.lowercase()) ?: return ApiResponse.error(402, "钱包不存在")
        var order = walletService.findSwapEPOrder(orderId)
        if (order != null) return ApiResponse.error(403, "orderId 重复")
        val txHash = LibWeb3.LIB_WEB_3.SendERC20Transfer(
            GoString.ByValue.of(properties.walletPrivateKey),
            GoString.ByValue.of(properties.tokenEP),
            GoString.ByValue.of(we.wallet),
            GoString.ByValue.of(amountStr)
        )
        if (txHash.isNullOrBlank()) {
            return ApiResponse.error(400, "交易发送失败")
        }
        order = SwapEPOrderEntity()
        order.walletId = we.id
        order.orderId = orderId
        order.txHash = txHash
        order.amount = amount
        walletService.saveSwapOrder(order)
        return ApiResponse(0, mapOf(
            "transferNo" to order.id,
            "txHash" to txHash
        ), null)
    }

}