package com.service.boot.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException;
import com.service.boot.exception.AppException;
import com.service.boot.i18n.I18nMessage;
import com.service.boot.model.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/* compiled from: GlobalExceptionController.kt */
@RestControllerAdvice(basePackages = {"com.service"})
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��.\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0010\u0003\n\u0002\b\u0002\b\u0017\u0018�� \u000f2\u00020\u0001:\u0001\u000fB\u0005¢\u0006\u0002\u0010\u0002J\"\u0010\b\u001a\u0004\u0018\u00010\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0017R\u0012\u0010\u0003\u001a\u00020\u00048\u0012@\u0012X\u0093.¢\u0006\u0002\n��R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0092\u0004¢\u0006\u0002\n��¨\u0006\u0010"}, d2 = {"Lcom/service/boot/controller/GlobalExceptionController;", "", "()V", "coreI18nMessage", "Lcom/service/boot/i18n/I18nMessage;", "logger", "Lorg/slf4j/Logger;", "kotlin.jvm.PlatformType", "globalException", "request", "Ljakarta/servlet/http/HttpServletRequest;", "response", "Ljakarta/servlet/http/HttpServletResponse;", "e", "", "Companion", "core"})
/* loaded from: kapai-core.jar:com/service/boot/controller/GlobalExceptionController.class */
public class GlobalExceptionController {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);
    @Autowired
    private I18nMessage coreI18nMessage;

    @JvmStatic
    @NotNull
    public static final Object getException(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Throwable e, @NotNull I18nMessage coreI18nMessage, @NotNull Logger logger) {
        return Companion.getException(request, response, e, coreI18nMessage, logger);
    }

    @JvmStatic
    private static final void logException(HttpServletRequest request, Throwable e, Logger logger) {
        Companion.logException(request, e, logger);
    }

    @ExceptionHandler({Throwable.class})
    @Nullable
    public Object globalException(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Throwable e) {
        Companion companion = Companion;
        I18nMessage i18nMessage = this.coreI18nMessage;
        if (i18nMessage == null) {
            Intrinsics.throwUninitializedPropertyAccessException("coreI18nMessage");
            i18nMessage = null;
        }
        return companion.getException(request, response, e, i18nMessage, this.logger);
    }

    /* compiled from: GlobalExceptionController.kt */
    @Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��0\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0003\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0010\u0003\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0010\u0002\n��\b\u0086\u0003\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J0\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0007J \u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\rH\u0003¨\u0006\u0010"}, d2 = {"Lcom/service/boot/controller/GlobalExceptionController$Companion;", "", "()V", "getException", "request", "Ljakarta/servlet/http/HttpServletRequest;", "response", "Ljakarta/servlet/http/HttpServletResponse;", "e", "", "coreI18nMessage", "Lcom/service/boot/i18n/I18nMessage;", "logger", "Lorg/slf4j/Logger;", "logException", "", "core"})
    @SourceDebugExtension({"SMAP\nGlobalExceptionController.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GlobalExceptionController.kt\ncom/service/boot/controller/GlobalExceptionController$Companion\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,161:1\n1549#2:162\n1620#2,3:163\n*S KotlinDebug\n*F\n+ 1 GlobalExceptionController.kt\ncom/service/boot/controller/GlobalExceptionController$Companion\n*L\n82#1:162\n82#1:163,3\n*E\n"})
    /* loaded from: kapai-core.jar:com/service/boot/controller/GlobalExceptionController$Companion.class */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final Object getException(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Throwable e, @NotNull I18nMessage coreI18nMessage, @NotNull Logger logger) {
            List list;
            logException(request, e, logger);
            if (e instanceof AppException) {
                if (((AppException) e).getCode() == 401) {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return ApiResponse.Companion.error(((AppException) e).getCode(), e.getMessage());
                }
                return ApiResponse.Companion.error(((AppException) e).getCode(), e.getMessage());
            } else if (e instanceof ConstraintViolationException) {
                response.setStatus(HttpStatus.OK.value());
                return ApiResponse.Companion.error(((ConstraintViolation) CollectionsKt.first(((ConstraintViolationException) e).getConstraintViolations())).getMessage());
            } else {
                if (e instanceof MethodArgumentNotValidException) {
                    response.setStatus(HttpStatus.OK.value());
                    FieldError error = ((MethodArgumentNotValidException) e).getFieldError();
                    if (error != null) {
                        return ApiResponse.Companion.error(error.getField() + " " + error.getDefaultMessage());
                    }
                } else if (e instanceof BindException) {
                    response.setStatus(HttpStatus.OK.value());
                    FieldError error2 = ((BindException) e).getFieldError();
                    if (error2 != null) {
                        return ApiResponse.Companion.error(error2.getField() + " " + error2.getDefaultMessage());
                    }
                } else if (e instanceof HttpMessageNotReadableException) {
                    response.setStatus(HttpStatus.OK.value());
                    InvalidFormatException cause = e.getCause();
                    if (cause != null) {
                        if (cause instanceof InvalidFormatException) {
                            Iterable path = cause.getPath();
                            if (path != null) {
                                Iterable $this$map$iv = path;
                                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                                for (Object item$iv$iv : $this$map$iv) {
                                    JsonMappingException.Reference it = (JsonMappingException.Reference) item$iv$iv;
                                    destination$iv$iv.add(coreI18nMessage.getMessage("invalid.format", it.getFieldName(), cause.getValue(), cause.getTargetType().getName()));
                                }
                                list = (List) destination$iv$iv;
                            } else {
                                list = null;
                            }
                            List messages = list;
                            if (messages != null) {
                                return ApiResponse.Companion.error(CollectionsKt.joinToString$default(messages, ";", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null));
                            }
                        } else if (cause instanceof MissingKotlinParameterException) {
                            return ApiResponse.Companion.error(((MissingKotlinParameterException) cause).getParameter().getName() + " " + coreI18nMessage.getMessage("NotNull"));
                        }
                    }
                    return ApiResponse.Companion.error(coreI18nMessage.getMessage("http.message.not.readable"));
                } else if (e instanceof MissingRequestHeaderException) {
                    return ApiResponse.Companion.error("header[" + ((MissingRequestHeaderException) e).getHeaderName() + "] " + coreI18nMessage.getMessage("NotNull"));
                } else {
                    if (e instanceof MissingServletRequestParameterException) {
                        return ApiResponse.Companion.error(coreI18nMessage.getMessage("http.request.params.miss", ((MissingServletRequestParameterException) e).getParameterName()));
                    }
                    if (e instanceof MethodArgumentTypeMismatchException) {
                        ApiResponse.Companion companion = ApiResponse.Companion;
                        Object[] objArr = new Object[1];
                        String parameterName = ((MethodArgumentTypeMismatchException) e).getParameter().getParameterName();
                        if (parameterName == null) {
                            parameterName = "";
                        }
                        objArr[0] = parameterName;
                        return companion.error(coreI18nMessage.getMessage("http.request.params.type.fail", objArr));
                    } else if (e instanceof HttpMediaTypeException) {
                        return ApiResponse.Companion.error(coreI18nMessage.getMessage("http.media.type.error"));
                    } else {
                        if (e instanceof InvalidMediaTypeException) {
                            return ApiResponse.Companion.error(coreI18nMessage.getMessage("http.media.type.error"));
                        }
                        if (e instanceof HttpRequestMethodNotSupportedException) {
                            return ApiResponse.Companion.error(coreI18nMessage.getMessage("http.request.method.not.supported"));
                        }
                        if (e instanceof NullPointerException) {
                            String message = e.getMessage();
                            if (message == null) {
                                message = "";
                            }
                            if (StringsKt.startsWith$default(StringsKt.trim(message).toString(), "Parameter specified as non-null is null", false, 2, (Object) null)) {
                                return ApiResponse.Companion.error(coreI18nMessage.getMessage("http.request.params.not.null"));
                            }
                        } else {
                            String name = e.getClass().getName();
                            if (Intrinsics.areEqual(name, "org.springframework.security.access.AccessDeniedException") ? true : Intrinsics.areEqual(name, "org.springframework.security.authentication.AuthenticationCredentialsNotFoundException")) {
                                response.setContentType("application/json;charset=UTF-8");
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                return ApiResponse.Companion.error(401, coreI18nMessage.getMessage("access.denied.error"));
                            }
                        }
                    }
                }
                return ApiResponse.Companion.error(coreI18nMessage.getMessage("server.error"));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        @JvmStatic
        public final void logException(HttpServletRequest request, Throwable e, Logger logger) {
            if (e instanceof AppException) {
                if (((AppException) e).getCode() == 401) {
                    logger.error("path: [{}] code: {}, error: {}, token: {}", new Object[]{request.getRequestURI(), Integer.valueOf(((AppException) e).getCode()), e.getMessage(), request.getHeader("token")});
                    return;
                } else {
                    logger.error("path: [{}] code: {}, error: {}", new Object[]{request.getRequestURI(), Integer.valueOf(((AppException) e).getCode()), e.getMessage()});
                    return;
                }
            }
            String name = e.getClass().getName();
            if (Intrinsics.areEqual(name, "org.springframework.security.access.AccessDeniedException") ? true : Intrinsics.areEqual(name, "org.springframework.security.authentication.AuthenticationCredentialsNotFoundException")) {
                logger.error("path: [{}] access denied, token: {}", request.getRequestURI(), request.getHeader("token"));
            } else {
                logger.error("path: [" + request.getRequestURI() + "]", e);
            }
        }
    }
}
