package com.service.boot.web.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.MapsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/* compiled from: AuthenticationToken.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010��\n��\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n��\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018��2\u00020\u0001B)\b\u0016\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\bB%\b\u0016\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0014\u0010\u000b\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00030\f¢\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00030\fJ\b\u0010\u0010\u001a\u0004\u0018\u00010\nJ\b\u0010\u0011\u001a\u00020\rH\u0016R\u001e\u0010\u000b\u001a\u0012\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n��R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n��¨\u0006\u0012"}, d2 = {"Lcom/service/boot/web/security/AuthenticationToken;", "Lorg/springframework/security/authentication/UsernamePasswordAuthenticationToken;", "principal", "", "authorities", "", "Lorg/springframework/security/core/GrantedAuthority;", "detail", "(Ljava/lang/Object;Ljava/util/Collection;Ljava/lang/Object;)V", "request", "Ljakarta/servlet/http/HttpServletRequest;", "params", "", "", "(Ljakarta/servlet/http/HttpServletRequest;Ljava/util/Map;)V", "getAuthParams", "getHttpServletRequest", "toString", "security-common"})
/* loaded from: kapai-security-common.jar:com/service/boot/web/security/AuthenticationToken.class */
public final class AuthenticationToken extends UsernamePasswordAuthenticationToken {
    @Nullable
    private Map<String, ? extends Object> params;
    @Nullable
    private HttpServletRequest request;

    public AuthenticationToken(@Nullable Object principal, @NotNull Collection<? extends GrantedAuthority> authorities, @Nullable Object detail) {
        super(principal, (Object) null, authorities);
        setDetails(detail);
    }

    public AuthenticationToken(@NotNull HttpServletRequest request, @NotNull Map<String, ? extends Object> params) {
        super((Object) null, (Object) null);
        this.request = request;
        this.params = params;
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" [");
        sb.append("Principal=").append(getPrincipal()).append(", ");
        sb.append("Credentials=[PROTECTED], ");
        sb.append("Authenticated=").append(isAuthenticated()).append(", ");
        sb.append("Details=").append(getDetails()).append(", ");
        sb.append("Granted Authorities=").append(getAuthorities()).append(", ");
        sb.append("Params=").append(this.params);
        sb.append("]");
        return sb.toString();
    }

    @NotNull
    public final Map<String, Object> getAuthParams() {
        Map<String, ? extends Object> map = this.params;
        return map == null ? MapsKt.emptyMap() : map;
    }

    @Nullable
    public final HttpServletRequest getHttpServletRequest() {
        return this.request;
    }
}
