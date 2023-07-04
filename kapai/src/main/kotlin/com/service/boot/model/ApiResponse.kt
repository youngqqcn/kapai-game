package com.service.boot.model

data class ApiResponse(
        var code: Int,
        var data: Any?,
        var message: String?
) {
    companion object {
        @JvmStatic
        fun success(data: Any? = null, message: String? = null): ApiResponse {
            return ApiResponse(200, data, message)
        }

        @JvmStatic
        fun error(code: Int, message: String? = null): ApiResponse {
            return ApiResponse(code, null, message)
        }

        @JvmStatic
        fun error(message: String? = null): ApiResponse {
            return ApiResponse(500, null, message)
        }
    }
}