package com.service.boot.web.security;

import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/* compiled from: AuthenticationUser.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��*\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n��\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0016\u0018�� \u00162\u00020\u0001:\u0001\u0016B3\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u000e\b\u0002\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0014\b\u0002\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\u0002\u0010\tJ\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001J\u000e\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\bR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n��\u001a\u0004\b\n\u0010\u000bR\u001d\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\b\n��\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0001¢\u0006\b\n��\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u0017"}, d2 = {"Lcom/service/boot/web/security/AuthenticationUser;", "", "principal", "authorities", "", "Lorg/springframework/security/core/GrantedAuthority;", "permissions", "", "", "(Ljava/lang/Object;Ljava/util/Set;Ljava/util/Map;)V", "getAuthorities", "()Ljava/util/Set;", "getPermissions", "()Ljava/util/Map;", "getPrincipal", "()Ljava/lang/Object;", "hasPermission", "", "target", "permission", "hasRole", "role", "Companion", "security-common"})
/* loaded from: kapai-security-common.jar:com/service/boot/web/security/AuthenticationUser.class */
public class AuthenticationUser {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Object principal;
    @NotNull
    private final Set<GrantedAuthority> authorities;
    @NotNull
    private final Map<String, String> permissions;

    @JvmStatic
    @Nullable
    public static final AuthenticationUser getAuthenticationUser() {
        return Companion.getAuthenticationUser();
    }

    public AuthenticationUser(@NotNull Object principal, @NotNull Set<? extends GrantedAuthority> authorities, @NotNull Map<String, String> permissions) {
        this.principal = principal;
        this.authorities = authorities;
        this.permissions = permissions;
    }

    public /* synthetic */ AuthenticationUser(Object obj, Set set, Map map, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(obj, (i & 2) != 0 ? SetsKt.emptySet() : set, (i & 4) != 0 ? MapsKt.emptyMap() : map);
    }

    @NotNull
    public final Object getPrincipal() {
        return this.principal;
    }

    @NotNull
    public final Set<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @NotNull
    public final Map<String, String> getPermissions() {
        return this.permissions;
    }

    public final boolean hasRole(@NotNull String role) {
        for (GrantedAuthority grantedAuthority : this.authorities) {
            if (Intrinsics.areEqual(grantedAuthority.getAuthority(), role)) {
                return true;
            }
        }
        return false;
    }

    public final boolean hasPermission(@NotNull Object target, @Nullable Object permission) {
        String value = this.permissions.get(target);
        if (value == null) {
            return false;
        }
        return Intrinsics.areEqual(value, permission);
    }

    /* compiled from: AuthenticationUser.kt */
    @Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��\u0012\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\b\u0086\u0003\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\n\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0007¨\u0006\u0005"}, d2 = {"Lcom/service/boot/web/security/AuthenticationUser$Companion;", "", "()V", "getAuthenticationUser", "Lcom/service/boot/web/security/AuthenticationUser;", "security-common"})
    /* loaded from: kapai-security-common.jar:com/service/boot/web/security/AuthenticationUser$Companion.class */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        @Nullable
        public final AuthenticationUser getAuthenticationUser() {
            AuthenticationToken authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthenticationToken authenticationToken = authentication instanceof AuthenticationToken ? authentication : null;
            Object details = authenticationToken != null ? authenticationToken.getDetails() : null;
            if (details instanceof AuthenticationUser) {
                return (AuthenticationUser) details;
            }
            return null;
        }
    }
}
