package com.service.kapai.configuration

import com.service.boot.i18n.I18nMessage
import com.service.boot.json.JSON
import com.service.boot.model.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.servlet.HandlerInterceptor
import java.time.LocalDateTime

class RequestHandlerInterceptor(
    private val i18nMessage: I18nMessage
) : HandlerInterceptor {

    /**
     * 分红半小时时间段内禁止下列接口
     */
    private val matchers by lazy {
        listOf(
            AntPathRequestMatcher.antMatcher("/card/mold/data"),
            AntPathRequestMatcher.antMatcher("/node/buy"),
            AntPathRequestMatcher.antMatcher("/wallet/bind"),
            AntPathRequestMatcher.antMatcher("/wallet/checkIn"),
        )
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        with(LocalDateTime.now()) {
            if (hour == 0) {
                if (minute in 0..30 && matcher(request)) {
                    response.status = 200
                    response.addHeader("Content-Type", "application/json;charset=utf-8")
                    response.writer.write(JSON.toJSONString(ApiResponse.error(i18nMessage.getMessage("contract_dividends"))))
                    return false
                }
            }
        }
        return super.preHandle(request, response, handler)
    }

    private fun matcher(request: HttpServletRequest): Boolean {
        for (matcher in matchers) {
            if (matcher.matches(request)) {
                return true
            }
        }
        return false
    }
}