package com.service.kapai.controller

import com.service.kapai.DESTROY_VALUE
import com.service.kapai.service.BuyNodeOrderService
import com.service.kapai.service.CardModelService
import com.service.kapai.service.MoldOrderService
import com.service.kapai.service.WalletService
import com.service.kapai.task.Tasks
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

//@Tag(name = "test")
//@RestController
//@RequestMapping("test")
class TestController(
    private val task: Tasks
) {

    @Operation(summary = "动态释放")
    @GetMapping("statistics")
    fun statistics() {
        task.statistics()
    }

    @Operation(summary = "设置销毁量")
    @GetMapping("config/destroy")
    fun configDestroy(@Parameter(`in` = ParameterIn.QUERY, description = "销毁数量") destroy: BigDecimal) {
        DESTROY_VALUE = destroy
    }
}