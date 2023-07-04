package com.service.boot.web.security;

import com.service.boot.json.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.LocaleResolver;

/* compiled from: AuthenticationHandler.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n��\n\u0002\u0010\u000e\n��\n\u0002\u0010��\n��\u0018��2\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u00052\u00020\u0006B\u001f\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\f¢\u0006\u0002\u0010\rJ\"\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\"\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\"\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\"\u0010\u001f\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010 \u001a\u0004\u0018\u00010\u0018H\u0016J\"\u0010!\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\"\u0010\"\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J,\u0010#\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010'2\b\u0010(\u001a\u0004\u0018\u00010)H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n��R\u0016\u0010\u000e\u001a\n \u0010*\u0004\u0018\u00010\u000f0\u000fX\u0082\u0004¢\u0006\u0002\n��R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n��R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u0004¢\u0006\u0002\n��¨\u0006*"}, d2 = {"Lcom/service/boot/web/security/AuthenticationHandler;", "Lorg/springframework/security/web/authentication/AuthenticationSuccessHandler;", "Lorg/springframework/security/web/authentication/AuthenticationFailureHandler;", "Lorg/springframework/security/web/authentication/logout/LogoutHandler;", "Lorg/springframework/security/web/authentication/logout/LogoutSuccessHandler;", "Lorg/springframework/security/web/access/AccessDeniedHandler;", "Lorg/springframework/security/web/AuthenticationEntryPoint;", "localeResolver", "Lorg/springframework/web/servlet/LocaleResolver;", "securityMessageSource", "Lorg/springframework/context/MessageSource;", "service", "Lcom/service/boot/web/security/AuthenticationUserService;", "(Lorg/springframework/web/servlet/LocaleResolver;Lorg/springframework/context/MessageSource;Lcom/service/boot/web/security/AuthenticationUserService;)V", "log", "Lorg/slf4j/Logger;", "kotlin.jvm.PlatformType", "commence", "", "request", "Ljakarta/servlet/http/HttpServletRequest;", "response", "Ljakarta/servlet/http/HttpServletResponse;", "authException", "Lorg/springframework/security/core/AuthenticationException;", "handle", "accessDeniedException", "Lorg/springframework/security/access/AccessDeniedException;", "logout", "authentication", "Lorg/springframework/security/core/Authentication;", "onAuthenticationFailure", "exception", "onAuthenticationSuccess", "onLogoutSuccess", "writeMessage", "code", "", "message", "", "data", "", "security-common"})
/* loaded from: kapai-security-common.jar:com/service/boot/web/security/AuthenticationHandler.class */
public final class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutHandler, LogoutSuccessHandler, AccessDeniedHandler, AuthenticationEntryPoint {
    @NotNull
    private LocaleResolver localeResolver;
    @NotNull
    private final MessageSource securityMessageSource;
    @Nullable
    private final AuthenticationUserService service;
    private final Logger log = LoggerFactory.getLogger(AuthenticationHandler.class);

    public AuthenticationHandler(@NotNull LocaleResolver localeResolver, @NotNull MessageSource securityMessageSource, @Nullable AuthenticationUserService service) {
        this.localeResolver = localeResolver;
        this.securityMessageSource = securityMessageSource;
        this.service = service;
    }

    public void onAuthenticationSuccess(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @Nullable Authentication authentication) {
        Object details = authentication != null ? authentication.getDetails() : null;
        AuthenticationUser user = details instanceof AuthenticationUser ? (AuthenticationUser) details : null;
        if (user != null) {
            this.log.info("登录认证 成功~~~ user: {} authentication: {}", user, authentication);
            AuthenticationUserService authenticationUserService = this.service;
            writeMessage(response, 200, null, authenticationUserService != null ? authenticationUserService.createAuthenticationInfo(user) : null);
            return;
        }
        this.log.warn("登录认证 失败!!!");
        writeMessage(response, 500, this.securityMessageSource.getMessage("bad.credentials.error", (Object[]) null, this.localeResolver.resolveLocale(request)), null);
    }

    public void onAuthenticationFailure(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @Nullable AuthenticationException exception) {
        Logger logger = this.log;
        String requestURI = request.getRequestURI();
        String message = exception != null ? exception.getMessage() : null;
        if (message == null) {
            message = "";
        }
        logger.error("[{}] 认证失败, exception: {}", requestURI, message);
        writeMessage(response, 500, this.securityMessageSource.getMessage("bad.credentials.error", (Object[]) null, this.localeResolver.resolveLocale(request)), null);
    }

    public void logout(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @Nullable Authentication authentication) {
        this.log.info("退出登录 authentication: {}", authentication);
        Object details = authentication != null ? authentication.getDetails() : null;
        AuthenticationUser user = details instanceof AuthenticationUser ? (AuthenticationUser) details : null;
        if (user != null) {
            AuthenticationUserService authenticationUserService = this.service;
            if (authenticationUserService != null ? authenticationUserService.onLogout(user) : false) {
                return;
            }
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        writeMessage(response, 401, this.securityMessageSource.getMessage("bad.credentials.error", (Object[]) null, this.localeResolver.resolveLocale(request)), null);
    }

    public void onLogoutSuccess(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @Nullable Authentication authentication) {
        if (response.getStatus() == HttpStatus.OK.value()) {
            this.log.info("退出登录成功 authentication: {}}", authentication);
            writeMessage(response, 200, null, null);
            return;
        }
        this.log.error("退出登录失败 authentication: {}", authentication);
    }

    public void handle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @Nullable AccessDeniedException accessDeniedException) {
        Logger logger = this.log;
        String requestURI = request.getRequestURI();
        String message = accessDeniedException != null ? accessDeniedException.getMessage() : null;
        if (message == null) {
            message = "";
        }
        logger.error("[{}] authentication access denied! accessDeniedException: {}", requestURI, message);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        writeMessage(response, 401, this.securityMessageSource.getMessage("access.denied.error", (Object[]) null, this.localeResolver.resolveLocale(request)), null);
    }

    public void commence(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @Nullable AuthenticationException authException) {
        Logger logger = this.log;
        String requestURI = request.getRequestURI();
        String message = authException != null ? authException.getMessage() : null;
        if (message == null) {
            message = "";
        }
        logger.error("[{}] no authentication! authException: {}", requestURI, message);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        writeMessage(response, 401, this.securityMessageSource.getMessage("access.denied.error", (Object[]) null, this.localeResolver.resolveLocale(request)), null);
    }

    private final void writeMessage(HttpServletResponse response, int code, String message, Object data) {
        Map map = new LinkedHashMap();
        map.put("code", Integer.valueOf(code));
        String str = message;
        if (!(str == null || StringsKt.isBlank(str))) {
            map.put("message", message);
        }
        if (data != null) {
            map.put("data", data);
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(map));
    }
}
