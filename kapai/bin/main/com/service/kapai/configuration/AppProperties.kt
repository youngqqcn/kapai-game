package com.service.kapai.configuration

import com.service.kapai.jna.LibWeb3
import com.service.kapai.jna.go.GoString
import com.service.boot.annotation.NoArgOpenDataClass
import com.service.boot.json.JSONObject
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.context.properties.ConfigurationProperties

@NoArgOpenDataClass
@ConfigurationProperties(prefix = "app")
data class AppProperties(
    val chainNodeHttpProxy: String?,
    val chainNode: String,
    val chainId: Long,
    val usdt: String,
    val tokenA: String,
    val tokenB: String,
    val tokenEP: String,
    var communityAddress: String,
    var technologyAddress: String,
    val rengGouContract: String,
    val releaseContract: String,
    val walletPrivateKey: String,
    val epRatio: Float,
    val swapPublicKey: String,
) : InitializingBean {
    override fun afterPropertiesSet() {
        val config = JSONObject()
        config.put("chain_node", chainNode)
        config.put("chain_id", chainId)
        if (!chainNodeHttpProxy.isNullOrBlank()) {
            config.put("http_proxy", chainNodeHttpProxy)
        }
        LibWeb3.LIB_WEB_3.InitConfig(GoString.ByValue.of(config.toJSONString()))
    }

}