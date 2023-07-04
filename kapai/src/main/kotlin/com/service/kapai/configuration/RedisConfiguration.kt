package com.service.kapai.configuration

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializerBase
import com.service.boot.number
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.math.BigDecimal

@Configuration
class RedisConfiguration {

    private fun <K, V> createRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<K, V> {
        val om = ObjectMapper()
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY)
        om.registerModule(SimpleModule().also {
            it.addSerializer(BigDecimal::class.java, object : ToStringSerializerBase(BigDecimal::class.java) {
                override fun valueToString(value: Any): String {
                    return (value as BigDecimal).number()
                }
            })
        })
        val template = RedisTemplate<K, V>()
        template.setConnectionFactory(redisConnectionFactory)
        template.keySerializer = StringRedisSerializer.UTF_8
        template.valueSerializer = GenericJackson2JsonRedisSerializer(om)
        template.hashKeySerializer = StringRedisSerializer.UTF_8
        template.hashValueSerializer = GenericJackson2JsonRedisSerializer(om)
        template.afterPropertiesSet()
        return template
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        return createRedisTemplate(redisConnectionFactory)
    }
}