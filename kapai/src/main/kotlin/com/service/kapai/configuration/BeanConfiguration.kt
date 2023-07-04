package com.service.kapai.configuration

import com.service.boot.i18n.I18nMessage
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import java.nio.charset.StandardCharsets
import java.util.*

@EnableConfigurationProperties(AppProperties::class)
@Configuration
class BeanConfiguration {

    @Bean
    fun i18nMessageSource(): MessageSource {
        val source = ResourceBundleMessageSource()
        source.setDefaultEncoding(StandardCharsets.UTF_8.name())
        source.addBasenames("i18n.core")
        source.addBasenames("i18n.base_validator")
        source.addBasenames("i18n.security")
        source.addBasenames("i18n.validator")
        source.setDefaultLocale(Locale.ENGLISH)
        return source
    }

    @Bean
    fun i18nMessage(i18nMessageSource: MessageSource): I18nMessage {
        return I18nMessage(i18nMessageSource)
    }

    @Bean
    fun requestHandlerInterceptor(i18nMessage: I18nMessage): RequestHandlerInterceptor{
        return RequestHandlerInterceptor(i18nMessage)
    }

}