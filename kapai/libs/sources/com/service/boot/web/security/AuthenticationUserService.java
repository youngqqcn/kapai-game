package com.service.boot.web.security;

import jakarta.servlet.http.HttpServletRequest;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthenticationUserService.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��*\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0003\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018��2\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&J&\u0010\u0006\u001a\u00020\u00072\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\b\u001a\u0004\u0018\u00010\u00012\b\u0010\t\u001a\u0004\u0018\u00010\u0001H\u0016J\u0012\u0010\n\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0012\u0010\r\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000e\u001a\u00020\u000fH&J\u0012\u0010\u0010\u001a\u00020\u00072\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0011"}, d2 = {"Lcom/service/boot/web/security/AuthenticationUserService;", "", "()V", "createAuthenticationInfo", "user", "Lcom/service/boot/web/security/AuthenticationUser;", "hasPermission", "", "targetDomainObject", "permission", "loadAuthentication", "request", "Ljakarta/servlet/http/HttpServletRequest;", "onAuthentication", "token", "Lcom/service/boot/web/security/AuthenticationToken;", "onLogout", "security-common"})
/* loaded from: kapai-security-common.jar:com/service/boot/web/security/AuthenticationUserService.class */
public abstract class AuthenticationUserService {
    @Nullable
    public abstract AuthenticationUser onAuthentication(@NotNull AuthenticationToken token);

    @Nullable
    public abstract Object createAuthenticationInfo(@Nullable AuthenticationUser user);

    @Nullable
    public AuthenticationUser loadAuthentication(@NotNull HttpServletRequest request) {
        return null;
    }

    public boolean onLogout(@Nullable AuthenticationUser user) {
        return true;
    }

    public boolean hasPermission(@Nullable AuthenticationUser user, @Nullable Object targetDomainObject, @Nullable Object permission) {
        return true;
    }
}
