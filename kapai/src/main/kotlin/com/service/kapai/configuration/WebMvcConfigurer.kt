package com.service.kapai.configuration

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializerBase
import com.service.boot.number
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import java.math.BigDecimal

@Configuration
class WebMvcConfigurer(
    private val requestHandlerInterceptor: RequestHandlerInterceptor
) : org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.forEach {
            (it as? MappingJackson2HttpMessageConverter)?.let {
                it.objectMapper.registerModule(SimpleModule().also {
                    it.addSerializer(BigDecimal::class.java, object: ToStringSerializerBase(BigDecimal::class.java) {
                        override fun valueToString(value: Any): String {
                            return (value as BigDecimal).number()
                        }
                    })
                })
            }
        }
        super.configureMessageConverters(converters)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(requestHandlerInterceptor).addPathPatterns("/**")
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("*")
            .maxAge(3600)
            .allowCredentials(true)
    }
}