package com.service.boot.web.security;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

/* compiled from: AuthenticationUserManager.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018��2\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n��¨\u0006\b"}, d2 = {"Lcom/service/boot/web/security/AuthenticationUserManager;", "Lorg/springframework/security/authentication/AuthenticationManager;", "service", "Lcom/service/boot/web/security/AuthenticationUserService;", "(Lcom/service/boot/web/security/AuthenticationUserService;)V", "authenticate", "Lorg/springframework/security/core/Authentication;", "authentication", "security-common"})
/* loaded from: kapai-security-common.jar:com/service/boot/web/security/AuthenticationUserManager.class */
public final class AuthenticationUserManager implements AuthenticationManager {
    @Nullable
    private final AuthenticationUserService service;

    public AuthenticationUserManager(@Nullable AuthenticationUserService service) {
        this.service = service;
    }

    @NotNull
    public Authentication authenticate(@NotNull Authentication authentication) {
        AuthenticationUser user;
        Intrinsics.checkNotNull(authentication, "null cannot be cast to non-null type com.service.boot.web.security.AuthenticationToken");
        AuthenticationToken token = (AuthenticationToken) authentication;
        AuthenticationUserService authenticationUserService = this.service;
        if (authenticationUserService == null || (user = authenticationUserService.onAuthentication(token)) == null) {
            throw new BadCredentialsException("登录认证失败");
        }
        return new AuthenticationToken(user.getPrincipal(), user.getAuthorities(), user);
    }
}
