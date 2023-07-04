package com.service.boot.web;

import com.service.boot.web.security.AuthenticationHandler;
import com.service.boot.web.security.AuthenticationUserManager;
import com.service.boot.web.security.AuthenticationUserService;
import com.service.boot.web.security.VerificationAuthenticationFilter;
import java.lang.reflect.Method;
import java.util.Arrays;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.servlet.LocaleResolver;

/* compiled from: SpringSecurityConfig.kt */
@Configuration
@EnableWebSecurity
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��0\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\b\u0017\u0018��2\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J(\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0017R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0012@\u0012X\u0093\u000e¢\u0006\u0002\n��¨\u0006\u000f"}, d2 = {"Lcom/service/boot/web/SpringSecurityConfig;", "Lcom/service/boot/web/BaseSecurityConfig;", "()V", "service", "Lcom/service/boot/web/security/AuthenticationUserService;", "securityFilterChain", "Lorg/springframework/security/web/SecurityFilterChain;", "http", "Lorg/springframework/security/config/annotation/web/builders/HttpSecurity;", "environment", "Lorg/springframework/core/env/Environment;", "localeResolver", "Lorg/springframework/web/servlet/LocaleResolver;", "securityMessageSource", "Lorg/springframework/context/MessageSource;", "security-session"})
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
/* loaded from: kapai-security-session.jar:com/service/boot/web/SpringSecurityConfig.class */
public class SpringSecurityConfig extends BaseSecurityConfig {
    @Autowired(required = false)
    @Nullable
    private AuthenticationUserService service;

    @Bean
    @Order(BaseSecurityConfig.ORDER_SECURITY_DEFAULT)
    @NotNull
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http, @NotNull Environment environment, @NotNull LocaleResolver localeResolver, @NotNull MessageSource securityMessageSource) {
        http.cors(Customizer.withDefaults());
        http.csrf(SpringSecurityConfig::securityFilterChain$lambda$0);
        http.httpBasic(SpringSecurityConfig::securityFilterChain$lambda$1);
        http.getConfigurer(new DefaultLoginPageConfigurer().getClass()).disable();
        AuthenticationUserManager authenticationManager = new AuthenticationUserManager(this.service);
        http.authenticationManager(authenticationManager);
        AuthenticationHandler authenticationHandler = new AuthenticationHandler(localeResolver, securityMessageSource, this.service);
        http.formLogin((v1) -> {
            securityFilterChain$lambda$2(r1, v1);
        });
        http.logout((v1) -> {
            securityFilterChain$lambda$3(r1, v1);
        });
        http.sessionManagement(SpringSecurityConfig::securityFilterChain$lambda$4);
        http.exceptionHandling((v1) -> {
            securityFilterChain$lambda$5(r1, v1);
        });
        http.authorizeHttpRequests((v1) -> {
            securityFilterChain$lambda$6(r1, v1);
        });
        http.headers(SpringSecurityConfig::securityFilterChain$lambda$8);
        return (SecurityFilterChain) http.build();
    }

    private static final void securityFilterChain$lambda$0(CsrfConfigurer it) {
        it.disable();
    }

    private static final void securityFilterChain$lambda$1(HttpBasicConfigurer it) {
        it.disable();
    }

    private static final void securityFilterChain$lambda$2(AuthenticationHandler $authenticationHandler, FormLoginConfigurer it) {
        Method method = it.getClass().getSuperclass().getDeclaredMethod("setAuthenticationFilter", AbstractAuthenticationProcessingFilter.class);
        method.setAccessible(true);
        method.invoke(it, new VerificationAuthenticationFilter());
        it.loginProcessingUrl(BaseSecurityConfig.LOGIN_PATH);
        it.successHandler($authenticationHandler);
        it.failureHandler($authenticationHandler);
    }

    private static final void securityFilterChain$lambda$3(AuthenticationHandler $authenticationHandler, LogoutConfigurer it) {
        it.logoutUrl(BaseSecurityConfig.LOGOUT_PATH).logoutSuccessHandler($authenticationHandler).addLogoutHandler($authenticationHandler);
    }

    private static final void securityFilterChain$lambda$4(SessionManagementConfigurer it) {
        it.maximumSessions(1);
    }

    private static final void securityFilterChain$lambda$5(AuthenticationHandler $authenticationHandler, ExceptionHandlingConfigurer it) {
        it.authenticationEntryPoint($authenticationHandler).accessDeniedHandler($authenticationHandler);
    }

    private static final void securityFilterChain$lambda$6(Environment $environment, AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry registry) {
        String[] strArr = BaseSecurityConfig.EXCLUDE_WEB_IGNORING_PATH_ARRAY;
        ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) registry.requestMatchers((String[]) Arrays.copyOf(strArr, strArr.length))).permitAll();
        String[] strArr2 = BaseSecurityConfig.EXCLUDE_PATH_ARRAY;
        ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) registry.requestMatchers((String[]) Arrays.copyOf(strArr2, strArr2.length))).permitAll();
        String[] stringArray = BaseSecurityConfig.Companion.getStringArray($environment, "spring.security.filter.exclude");
        ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) registry.requestMatchers((String[]) Arrays.copyOf(stringArray, stringArray.length))).permitAll();
        ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) registry.anyRequest()).authenticated();
    }

    private static final void securityFilterChain$lambda$8(HeadersConfigurer headersConfigurer) {
        headersConfigurer.frameOptions(SpringSecurityConfig::securityFilterChain$lambda$8$lambda$7);
    }

    private static final void securityFilterChain$lambda$8$lambda$7(HeadersConfigurer.FrameOptionsConfig frameOptionsConfig) {
        frameOptionsConfig.disable();
    }
}
