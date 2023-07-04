package com.service.kapai.controller

import com.service.kapai.model.AuthInfoModel
import com.service.boot.exception.AppException
import com.service.boot.web.security.AuthenticationUser
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(info = Info(
    version = "1.0",
    title = "卡牌服务接口",
))
abstract class BaseController {

    fun currentUser(): AuthInfoModel {
        val model = AuthenticationUser.getAuthenticationUser()?.principal as? AuthInfoModel
        model ?: throw AppException(401, "no auth")
        return model
    }

}