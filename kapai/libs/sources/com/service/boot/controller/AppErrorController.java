package com.service.boot.controller;

import com.service.boot.controller.GlobalExceptionController;
import com.service.boot.i18n.I18nMessage;
import com.service.boot.model.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

/* compiled from: AppErrorController.kt */
@RequestMapping({"${server.error.path:${error.path:/error}}"})
@Hidden
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010��\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018��2\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u001a\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0017J\u0018\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0017J\u0018\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u001cH\u0012J\u0018\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0017R\u0012\u0010\b\u001a\u00020\t8\u0012@\u0012X\u0093.¢\u0006\u0002\n��R\u001a\u0010\u0002\u001a\u00020\u0003X\u0096\u000e¢\u0006\u000e\n��\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0016\u0010\u000e\u001a\n \u0010*\u0004\u0018\u00010\u000f0\u000fX\u0092\u0004¢\u0006\u0002\n��¨\u0006\u001e"}, d2 = {"Lcom/service/boot/controller/AppErrorController;", "Lorg/springframework/boot/autoconfigure/web/servlet/error/AbstractErrorController;", "errorAttributes", "Lorg/springframework/boot/web/servlet/error/ErrorAttributes;", "errorViewResolvers", "", "Lorg/springframework/boot/autoconfigure/web/servlet/error/ErrorViewResolver;", "(Lorg/springframework/boot/web/servlet/error/ErrorAttributes;Ljava/util/List;)V", "coreI18nMessage", "Lcom/service/boot/i18n/I18nMessage;", "getErrorAttributes", "()Lorg/springframework/boot/web/servlet/error/ErrorAttributes;", "setErrorAttributes", "(Lorg/springframework/boot/web/servlet/error/ErrorAttributes;)V", "logger", "Lorg/slf4j/Logger;", "kotlin.jvm.PlatformType", "error", "", "request", "Ljakarta/servlet/http/HttpServletRequest;", "response", "Ljakarta/servlet/http/HttpServletResponse;", "errorHtml", "Lorg/springframework/web/servlet/ModelAndView;", "getErrorAttributeOptions", "Lorg/springframework/boot/web/error/ErrorAttributeOptions;", "mediaType", "Lorg/springframework/http/MediaType;", "mediaTypeNotAcceptable", "core"})
@RestController
/* loaded from: kapai-core.jar:com/service/boot/controller/AppErrorController.class */
public class AppErrorController extends AbstractErrorController {
    @NotNull
    private ErrorAttributes errorAttributes;
    private final Logger logger;
    @Autowired
    private I18nMessage coreI18nMessage;

    public AppErrorController(@NotNull ErrorAttributes errorAttributes, @NotNull List<? extends ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        this.errorAttributes = errorAttributes;
        this.logger = LoggerFactory.getLogger(AppErrorController.class);
    }

    @NotNull
    public ErrorAttributes getErrorAttributes() {
        return this.errorAttributes;
    }

    public void setErrorAttributes(@NotNull ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(produces = {"text/html"})
    @NotNull
    public ModelAndView errorHtml(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map model = Collections.unmodifiableMap(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return modelAndView == null ? new ModelAndView("error", model) : modelAndView;
    }

    @RequestMapping
    @Nullable
    public Object error(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        int code = status.value();
        int value = status.value();
        if (200 <= value ? value < 300 : false) {
            code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        Throwable error = getErrorAttributes().getError(new ServletWebRequest(request));
        if (error != null) {
            GlobalExceptionController.Companion companion = GlobalExceptionController.Companion;
            I18nMessage i18nMessage = this.coreI18nMessage;
            if (i18nMessage == null) {
                Intrinsics.throwUninitializedPropertyAccessException("coreI18nMessage");
                i18nMessage = null;
            }
            return companion.getException(request, response, error, i18nMessage, this.logger);
        }
        switch (code) {
            case 401:
            case 402:
            case 403:
                ApiResponse.Companion companion2 = ApiResponse.Companion;
                int i = code;
                I18nMessage i18nMessage2 = this.coreI18nMessage;
                if (i18nMessage2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("coreI18nMessage");
                    i18nMessage2 = null;
                }
                return companion2.error(i, i18nMessage2.getMessage("access.denied.error"));
            case 404:
                ApiResponse.Companion companion3 = ApiResponse.Companion;
                int i2 = code;
                I18nMessage i18nMessage3 = this.coreI18nMessage;
                if (i18nMessage3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("coreI18nMessage");
                    i18nMessage3 = null;
                }
                return companion3.error(i2, i18nMessage3.getMessage("resource.not.found"));
            default:
                ApiResponse.Companion companion4 = ApiResponse.Companion;
                int i3 = code;
                I18nMessage i18nMessage4 = this.coreI18nMessage;
                if (i18nMessage4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("coreI18nMessage");
                    i18nMessage4 = null;
                }
                return companion4.error(i3, i18nMessage4.getMessage("server.error"));
        }
    }

    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    @NotNull
    public Object mediaTypeNotAcceptable(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        ApiResponse.Companion companion = ApiResponse.Companion;
        int value = HttpStatus.INTERNAL_SERVER_ERROR.value();
        I18nMessage i18nMessage = this.coreI18nMessage;
        if (i18nMessage == null) {
            Intrinsics.throwUninitializedPropertyAccessException("coreI18nMessage");
            i18nMessage = null;
        }
        return companion.error(value, i18nMessage.getMessage("http.media.type.error"));
    }

    private ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request, MediaType mediaType) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        return options.including(new ErrorAttributeOptions.Include[]{ErrorAttributeOptions.Include.EXCEPTION}).including(new ErrorAttributeOptions.Include[]{ErrorAttributeOptions.Include.STACK_TRACE}).including(new ErrorAttributeOptions.Include[]{ErrorAttributeOptions.Include.MESSAGE}).including(new ErrorAttributeOptions.Include[]{ErrorAttributeOptions.Include.BINDING_ERRORS});
    }
}
