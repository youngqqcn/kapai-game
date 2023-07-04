package com.service.kapai.model

data class AuthInfoModel(val id: Long, val wallet: String) {
    var isBind = false
    var equityDesc: String? = null
    var levelDesc: String? = null
}