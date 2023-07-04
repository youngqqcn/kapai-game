package com.service.boot.web.security

import com.service.boot.json.JSON
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.web.servlet.LocaleResolver
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class AuthenticationHandler(private var localeResolver: LocaleResolver, private val i18nMessageSource: MessageSource, private val service: AuthenticationUserService?) : AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutHandler, LogoutSuccessHandler, AccessDeniedHandler, AuthenticationEntryPoint {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication?) {
        val user = authentication?.details as? AuthenticationUser
        if (user != null) {
            writeMessage(response, 200, null, service?.createAuthenticationInfo(user))
        } else {
            writeMessage(response, 500, i18nMessageSource.getMessage("bad.credentials.error", null, localeResolver.resolveLocale(request)), null)
        }
    }

    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException?) {
        writeMessage(response, 500, i18nMessageSource.getMessage("bad.credentials.error", null, localeResolver.resolveLocale(request)), null)
    }

    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication?) {
        val user = authentication?.details as? AuthenticationUser
        if (user == null || service?.onLogout(user) != true) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            writeMessage(response, 401, i18nMessageSource.getMessage("bad.credentials.error", null, localeResolver.resolveLocale(request)), null)
        }
    }

    override fun onLogoutSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication?) {
        if (response.status == HttpStatus.OK.value()) {
            writeMessage(response, 200, null, null)
        }
    }

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException?) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        writeMessage(response, 401, i18nMessageSource.getMessage("access.denied.error", null, localeResolver.resolveLocale(request)), null)
    }

    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException?) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        writeMessage(response, 401, i18nMessageSource.getMessage("access.denied.error", null, localeResolver.resolveLocale(request)), null)
    }

    private fun writeMessage(response: HttpServletResponse, code: Int, message: String?, data: Any?) {
        val map = mutableMapOf<String, Any>()
        map["code"] = code
        if (!message.isNullOrBlank()) {
            map["message"] = message
        }
        if (data != null) {
            map["data"] = data
        }
        response.contentType = MediaType.APPLICATION_JSON_UTF8_VALUE
        response.writer.write(JSON.toJSONString(map))
    }
}