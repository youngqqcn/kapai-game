package com.service.boot.web.security;

import java.io.Serializable;
import kotlin.Metadata;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/* compiled from: AuthenticationPermissionEvaluator.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��2\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000b\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000e\n��\n\u0002\u0010��\n\u0002\b\u0002\b\u0017\u0018��2\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J0\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J&\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0012@\u0012X\u0093\u000e¢\u0006\u0002\n��¨\u0006\u0010"}, d2 = {"Lcom/service/boot/web/security/AuthenticationPermissionEvaluator;", "Lorg/springframework/security/access/PermissionEvaluator;", "()V", "service", "Lcom/service/boot/web/security/AuthenticationUserService;", "hasPermission", "", "authentication", "Lorg/springframework/security/core/Authentication;", "targetId", "Ljava/io/Serializable;", "targetType", "", "permission", "", "targetDomainObject", "security-common"})
@Component
/* loaded from: kapai-security-common.jar:com/service/boot/web/security/AuthenticationPermissionEvaluator.class */
public class AuthenticationPermissionEvaluator implements PermissionEvaluator {
    @Autowired(required = false)
    @Nullable
    private AuthenticationUserService service;

    public boolean hasPermission(@Nullable Authentication authentication, @Nullable Object targetDomainObject, @Nullable Object permission) {
        AuthenticationToken authenticationToken = authentication instanceof AuthenticationToken ? (AuthenticationToken) authentication : null;
        Object details = authenticationToken != null ? authenticationToken.getDetails() : null;
        AuthenticationUser user = details instanceof AuthenticationUser ? (AuthenticationUser) details : null;
        AuthenticationUserService authenticationUserService = this.service;
        if (authenticationUserService != null) {
            return authenticationUserService.hasPermission(user, targetDomainObject, permission);
        }
        return false;
    }

    public boolean hasPermission(@Nullable Authentication authentication, @Nullable Serializable targetId, @Nullable String targetType, @Nullable Object permission) {
        return false;
    }
}
