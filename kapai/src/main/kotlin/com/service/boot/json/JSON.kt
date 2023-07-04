package com.service.boot.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory

object JSON {

    private val LOGGER = LoggerFactory.getLogger(JSON::class.java)
    private val objectMapper by lazy { ObjectMapper() }

    @JvmStatic
    fun <T> parseObject(json: String?, clazz: Class<T>): T? {
        return try {
            objectMapper.readValue(json, clazz)
        } catch (e: Exception) {
            LOGGER.error("JSON解析异常！", e)
            null
        }
    }

    @JvmStatic
    fun parseObject(json: String): JSONObject? {
        return parseObject(json, JSONObject::class.java)
    }

    @JvmStatic
    fun toJSONString(any: Any): String {
        return try {
            objectMapper.writeValueAsString(any)
        } catch (e: Exception) {
            LOGGER.error("JSON序列化异常！", e)
            ""
        }
    }

}