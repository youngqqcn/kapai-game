package com.service.boot.controller

import com.service.boot.exception.AppException
import com.service.boot.i18n.I18nMessage
import com.service.boot.model.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.service"])
class GlobalExceptionController {

    private val logger = LoggerFactory.getLogger(GlobalExceptionController::class.java)

    @Autowired
    private lateinit var i18nMessage: I18nMessage

    @ExceptionHandler(
        value = [
            Throwable::class
        ]
    )
    fun globalException(request: HttpServletRequest, response: HttpServletResponse, e: Throwable): Any? {
        return getException(request, response, e, i18nMessage, logger)
    }

    companion object {
        @JvmStatic
        fun getException(request: HttpServletRequest, response: HttpServletResponse, e: Throwable, i18nMessage: I18nMessage, logger: Logger): Any {
            when (e) {
                is AppException -> {
                    if (e.code == 401) {
                        response.contentType = MediaType.APPLICATION_JSON_UTF8_VALUE
                        response.status = HttpStatus.UNAUTHORIZED.value()
                        return ApiResponse.error(e.code, e.message)
                    }
                    return ApiResponse.error(e.code, e.message)
                }

                else -> {
                    when (e.javaClass.name) {
                        "org.springframework.security.access.AccessDeniedException",
                        "org.springframework.security.authentication.AuthenticationCredentialsNotFoundException" -> {
                            response.contentType = MediaType.APPLICATION_JSON_UTF8_VALUE
                            response.status = HttpStatus.UNAUTHORIZED.value()
                            return ApiResponse.error(401, i18nMessage.getMessage("access.denied.error"))
                        }
                    }
                }
            }
            return ApiResponse.error(i18nMessage.getMessage("server.error"))
        }
    }
}
