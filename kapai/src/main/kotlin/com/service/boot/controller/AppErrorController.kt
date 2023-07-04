package com.service.boot.controller

import com.service.boot.i18n.I18nMessage
import com.service.boot.model.ApiResponse
import io.swagger.v3.oas.annotations.Hidden
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.servlet.ModelAndView
import java.util.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Hidden
@RestController
@RequestMapping("\${server.error.path:\${error.path:/error}}")
class AppErrorController(var errorAttributes: ErrorAttributes, errorViewResolvers: List<ErrorViewResolver>) : AbstractErrorController(errorAttributes, errorViewResolvers) {

    private val logger = LoggerFactory.getLogger(AppErrorController::class.java)

    @Autowired
    private lateinit var i18nMessage: I18nMessage

    @RequestMapping(produces = [MediaType.TEXT_HTML_VALUE])
    fun errorHtml(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        val status = getStatus(request)
        val model = Collections.unmodifiableMap(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML)))
        response.status = status.value()
        val modelAndView = resolveErrorView(request, response, status, model)
        return modelAndView ?: ModelAndView("error", model)
    }

    @RequestMapping
    fun error(request: HttpServletRequest, response: HttpServletResponse): Any? {
        val status = getStatus(request);
        var code = status.value()
        if (status.value() in 200..299) {
            code = HttpStatus.INTERNAL_SERVER_ERROR.value()
        }
        val error = errorAttributes.getError(ServletWebRequest(request))
        if (error != null) {
            return GlobalExceptionController.getException(request, response, error, i18nMessage, logger)
        } else {
            when (code) {
                401, 402, 403 -> {
                    return ApiResponse.error(code, i18nMessage.getMessage("access.denied.error"))
                }
                404 -> {
                    return ApiResponse.error(code, i18nMessage.getMessage("resource.not.found"))
                }
            }
        }
        return ApiResponse.error(code, i18nMessage.getMessage("server.error"))
    }

    @ExceptionHandler(value = [HttpMediaTypeNotAcceptableException::class])
    fun mediaTypeNotAcceptable(request: HttpServletRequest, response: HttpServletResponse): Any {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), i18nMessage.getMessage("http.media.type.error"))
    }

    private fun getErrorAttributeOptions(request: HttpServletRequest, mediaType: MediaType): ErrorAttributeOptions {
        var options = ErrorAttributeOptions.defaults()
        options = options.including(ErrorAttributeOptions.Include.EXCEPTION)
        options = options.including(ErrorAttributeOptions.Include.STACK_TRACE)
        options = options.including(ErrorAttributeOptions.Include.MESSAGE)
        options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS)
        return options
    }
}