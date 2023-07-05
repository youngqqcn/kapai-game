package com.service.boot.web

import org.springframework.core.env.Environment

@com.service.boot.annotation.NoArgOpenDataClass
abstract class BaseSecurityConfig {

    companion object {

        const val ORDER_SECURITY_DEFAULT = 100

        const val LOGIN_PATH = "/api/auth/sign"

        const val LOGOUT_PATH = "/api/auth/logout"

        @JvmField
        val EXCLUDE_WEB_IGNORING_PATH_ARRAY = arrayOf(
            "/favicon.ico",
            "/*/*.html",
            "/*/*.css",
            "/*/*.js",
            "/*/*.jpg",
            "/*/*.png",
            "/*/*.jpeg"
        )

        @JvmField
        val EXCLUDE_PATH_ARRAY = arrayOf(
            "/error",
            "/csrf",
            "/swagger**/**",
            "/webjars**/**",
            "/v3/api-docs/**",
        )

        @JvmStatic
        fun getStringArray(environment: Environment, key: String): Array<String> {
            var index = 0
            val array = mutableListOf<String>()
            while (true) {
                val value = environment.getProperty("$key[$index]") ?: break
                array.add(value)
                index++
            }
            return array.toTypedArray()
        }
    }
}