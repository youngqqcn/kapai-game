package com.service.kapai.configuration

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.service.boot.json.JSON
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfiguration {

    private val objectMapper by lazy {
        JSON.createDefaultObjectMapper().also { om ->
            om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY)
        }
    }

    private fun <K, V> createRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<K, V> {
        val template = RedisTemplate<K, V>()
        template.setConnectionFactory(redisConnectionFactory)
        template.keySerializer = StringRedisSerializer.UTF_8
        template.valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        template.hashKeySerializer = StringRedisSerializer.UTF_8
        template.hashValueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
        template.afterPropertiesSet()
        return template
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        return createRedisTemplate(redisConnectionFactory)
    }

//    @Bean
//    fun taskRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
//        val factory = redisConnectionFactory as LettuceConnectionFactory
//        val newFactory = LettuceConnectionFactory(factory.standaloneConfiguration, factory.clientConfiguration)
//        newFactory.database = 1
//        newFactory.afterPropertiesSet()
//        return createRedisTemplate(newFactory)
//    }
}