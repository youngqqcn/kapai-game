package com.service.kapai.configuration

import com.service.boot.i18n.I18nMessage
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import java.nio.charset.StandardCharsets
import java.util.*

@EnableConfigurationProperties(AppProperties::class)
@Configuration
class BeanConfiguration {

    @Bean
    fun i18nMessage(): I18nMessage {
        val source = ResourceBundleMessageSource()
        source.setDefaultEncoding(StandardCharsets.UTF_8.name())
        source.setBasename("i18n.validator")
        source.setDefaultLocale(Locale.ENGLISH)
        return I18nMessage(source)
    }

    @Bean
    fun requestHandlerInterceptor(i18nMessage: I18nMessage): RequestHandlerInterceptor{
        return RequestHandlerInterceptor(i18nMessage)
    }

}