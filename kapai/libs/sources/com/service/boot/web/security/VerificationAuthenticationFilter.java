package com.service.boot.web.security;

import com.service.boot.json.JSON;
import com.service.boot.json.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.io.TextStreamsKt;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/* compiled from: VerificationAuthenticationFilter.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\u0018��2\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0082\u0004¢\u0006\u0002\n��¨\u0006\u000f"}, d2 = {"Lcom/service/boot/web/security/VerificationAuthenticationFilter;", "Lorg/springframework/security/web/authentication/UsernamePasswordAuthenticationFilter;", "()V", "filterProcessesUrl", "", "(Ljava/lang/String;)V", "log", "Lorg/slf4j/Logger;", "kotlin.jvm.PlatformType", "attemptAuthentication", "Lorg/springframework/security/core/Authentication;", "request", "Ljakarta/servlet/http/HttpServletRequest;", "response", "Ljakarta/servlet/http/HttpServletResponse;", "security-common"})
/* loaded from: kapai-security-common.jar:com/service/boot/web/security/VerificationAuthenticationFilter.class */
public final class VerificationAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Logger log = LoggerFactory.getLogger(VerificationAuthenticationFilter.class);

    public VerificationAuthenticationFilter() {
    }

    public VerificationAuthenticationFilter(@NotNull String filterProcessesUrl) {
        setFilterProcessesUrl(filterProcessesUrl);
    }

    @NotNull
    public Authentication attemptAuthentication(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        if (HttpMethod.POST.matches(request.getMethod())) {
            MediaType contentType = MediaType.parseMediaType(request.getContentType());
            if (contentType.includes(MediaType.APPLICATION_FORM_URLENCODED) || contentType.includes(MediaType.MULTIPART_FORM_DATA)) {
                Map params = new LinkedHashMap();
                Enumeration keys = request.getParameterNames();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    params.put(key, request.getParameter(key));
                }
                this.log.info("-\r\n登录参数\r\n{}", JSON.toJSONString(params));
                return getAuthenticationManager().authenticate(new AuthenticationToken(request, params));
            } else if (contentType.includes(MediaType.APPLICATION_JSON)) {
                InputStreamReader inputStreamReader = new InputStreamReader(request.getInputStream(), Charsets.UTF_8);
                String json = TextStreamsKt.readText(inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, 8192));
                JSONObject params2 = JSON.parseObject(json);
                if (params2 == null) {
                    throw new AuthenticationServiceException("Authentication json content fail: " + json);
                }
                this.log.info("-\r\n登录参数\r\n{}", json);
                return getAuthenticationManager().authenticate(new AuthenticationToken(request, params2));
            } else {
                throw new AuthenticationServiceException("Authentication content type not supported: " + request.getContentType());
            }
        }
        throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }
}
