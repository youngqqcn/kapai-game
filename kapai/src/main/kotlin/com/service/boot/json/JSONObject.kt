package com.service.boot.json

import com.service.boot.common.NumberUtils
import java.math.BigDecimal

class JSONObject : HashMap<String, Any?>() {

    @Suppress("UNCHECKED_CAST")
    private fun <T : Number> getNumberValue(key: String, clazz: Class<T>): T? {
        val value = this[key] ?: return null
        if (value is Number) {
            if (value.javaClass == clazz) return value as? T
            return when (clazz) {
                Int::class.java -> value.toInt() as? T
                Long::class.java -> value.toLong() as? T
                Double::class.java -> value.toDouble() as? T
                Float::class.java -> value.toFloat() as? T
                BigDecimal::class.java -> BigDecimal("$value") as? T
                else -> null
            }
        } else if (value is String) {
            return NumberUtils.parseNumber(value, clazz)
        }
        return value as? T
    }

    fun getString(key: String): String? {
        val value = this[key] ?: return null
        if (value is String) return value
        return value.toString()
    }

    fun getStringValue(key: String): String {
        val value = this[key] ?: return ""
        if (value is String) return value
        return value.toString()
    }

    fun getInteger(key: String): Int? {
        return getNumberValue(key, Int::class.java)
    }

    fun getIntegerValue(key: String): Int {
        return getNumberValue(key, Int::class.java) ?: 0
    }

    fun getLong(key: String): Long? {
        return getNumberValue(key, Long::class.java)
    }

    fun getLongValue(key: String): Long {
        return getNumberValue(key, Long::class.java) ?: 0L
    }

    fun toJSONString(): String {
        return JSON.toJSONString(this)
    }
}