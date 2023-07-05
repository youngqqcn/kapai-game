package com.service.boot.i18n

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

class I18nMessage(private val source: MessageSource) {

    fun getMessage(code: String): String {
        return source.getMessage(code, null, LocaleContextHolder.getLocale())
    }

    fun getMessage(code: String, vararg args: Any): String {
        return source.getMessage(code, args, LocaleContextHolder.getLocale())
    }
}