package com.service.boot.web;

import com.service.boot.annotation.NoArgOpenDataClass;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/* compiled from: BaseSecurityConfig.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"�� \n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\b'\u0018�� \t2\u00020\u0001:\u0001\tB\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0017J\b\u0010\u0007\u001a\u00020\bH\u0017¨\u0006\n"}, d2 = {"Lcom/service/boot/web/BaseSecurityConfig;", "", "()V", "healthSecurityFilterChain", "Lorg/springframework/security/web/SecurityFilterChain;", "http", "Lorg/springframework/security/config/annotation/web/builders/HttpSecurity;", "securityMessageSource", "Lorg/springframework/context/support/ResourceBundleMessageSource;", "Companion", "security-common"})
@NoArgOpenDataClass
/* loaded from: kapai-security-common.jar:com/service/boot/web/BaseSecurityConfig.class */
public abstract class BaseSecurityConfig {
    public static final int ORDER_SECURITY_DEFAULT = 100;
    public static final int ORDER_SECURITY_HEALTH = 99;
    public static final int ORDER_SECURITY_OTHER = 98;
    @NotNull
    public static final String LOGIN_PATH = "/api/auth/sign";
    @NotNull
    public static final String LOGOUT_PATH = "/api/auth/logout";
    @NotNull
    public static final Companion Companion = new Companion(null);
    @JvmField
    @NotNull
    public static final String[] EXCLUDE_WEB_IGNORING_PATH_ARRAY = {"/favicon.ico", "/*/*.html", "/*/*.css", "/*/*.js", "/*/*.jpg", "/*/*.png", "/*/*.jpeg"};
    @JvmField
    @NotNull
    public static final String[] EXCLUDE_PATH_ARRAY = {"/error", "/csrf", "/swagger**/**", "/webjars**/**", "/v3/api-docs/**"};

    @JvmStatic
    @NotNull
    public static final String[] getStringArray(@NotNull Environment environment, @NotNull String key) {
        return Companion.getStringArray(environment, key);
    }

    @Bean
    @NotNull
    public ResourceBundleMessageSource securityMessageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        source.setBasename("i18n.security");
        source.setDefaultLocale(Locale.ENGLISH);
        return source;
    }

    @Bean
    @Order(ORDER_SECURITY_HEALTH)
    @NotNull
    public SecurityFilterChain healthSecurityFilterChain(@NotNull HttpSecurity http) {
        AuthenticationManagerBuilder auth = (AuthenticationManagerBuilder) http.getSharedObject(AuthenticationManagerBuilder.class);
        PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        auth.inMemoryAuthentication().passwordEncoder(bCryptPasswordEncoder).withUser("cloud").password(bCryptPasswordEncoder.encode("12345qwert")).roles(new String[]{"HEALTH"});
        http.cors(Customizer.withDefaults());
        http.csrf(BaseSecurityConfig::healthSecurityFilterChain$lambda$0);
        http.securityContext(BaseSecurityConfig::healthSecurityFilterChain$lambda$1);
        http.sessionManagement(BaseSecurityConfig::healthSecurityFilterChain$lambda$2);
        http.requestCache(BaseSecurityConfig::healthSecurityFilterChain$lambda$3);
        http.anonymous(BaseSecurityConfig::healthSecurityFilterChain$lambda$4);
        http.formLogin(BaseSecurityConfig::healthSecurityFilterChain$lambda$5);
        http.rememberMe(BaseSecurityConfig::healthSecurityFilterChain$lambda$6);
        String[] paths = {"/actuator", "/actuator/**", "/instances", "/instances/**"};
        http.securityMatcher((String[]) Arrays.copyOf(paths, paths.length)).authorizeHttpRequests((v1) -> {
            healthSecurityFilterChain$lambda$7(r1, v1);
        });
        http.httpBasic(Customizer.withDefaults());
        return (SecurityFilterChain) http.build();
    }

    private static final void healthSecurityFilterChain$lambda$0(CsrfConfigurer it) {
        it.disable();
    }

    private static final void healthSecurityFilterChain$lambda$1(SecurityContextConfigurer it) {
        it.disable();
    }

    private static final void healthSecurityFilterChain$lambda$2(SessionManagementConfigurer it) {
        it.disable();
    }

    private static final void healthSecurityFilterChain$lambda$3(RequestCacheConfigurer it) {
        it.disable();
    }

    private static final void healthSecurityFilterChain$lambda$4(AnonymousConfigurer it) {
        it.disable();
    }

    private static final void healthSecurityFilterChain$lambda$5(FormLoginConfigurer it) {
        it.disable();
    }

    private static final void healthSecurityFilterChain$lambda$6(RememberMeConfigurer it) {
        it.disable();
    }

    private static final void healthSecurityFilterChain$lambda$7(String[] $paths, AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry it) {
        ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) it.requestMatchers((String[]) Arrays.copyOf($paths, $paths.length))).authenticated().anyRequest()).permitAll();
    }

    /* compiled from: BaseSecurityConfig.kt */
    @Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��(\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J#\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0005H\u0007¢\u0006\u0002\u0010\u0012R\u0018\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00048\u0006X\u0087\u0004¢\u0006\u0004\n\u0002\u0010\u0006R\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\u00048\u0006X\u0087\u0004¢\u0006\u0004\n\u0002\u0010\u0006R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n��R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n��R\u000e\u0010\n\u001a\u00020\u000bX\u0086T¢\u0006\u0002\n��R\u000e\u0010\f\u001a\u00020\u000bX\u0086T¢\u0006\u0002\n��R\u000e\u0010\r\u001a\u00020\u000bX\u0086T¢\u0006\u0002\n��¨\u0006\u0013"}, d2 = {"Lcom/service/boot/web/BaseSecurityConfig$Companion;", "", "()V", "EXCLUDE_PATH_ARRAY", "", "", "[Ljava/lang/String;", "EXCLUDE_WEB_IGNORING_PATH_ARRAY", "LOGIN_PATH", "LOGOUT_PATH", "ORDER_SECURITY_DEFAULT", "", "ORDER_SECURITY_HEALTH", "ORDER_SECURITY_OTHER", "getStringArray", "environment", "Lorg/springframework/core/env/Environment;", "key", "(Lorg/springframework/core/env/Environment;Ljava/lang/String;)[Ljava/lang/String;", "security-common"})
    @SourceDebugExtension({"SMAP\nBaseSecurityConfig.kt\nKotlin\n*S Kotlin\n*F\n+ 1 BaseSecurityConfig.kt\ncom/service/boot/web/BaseSecurityConfig$Companion\n+ 2 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n*L\n1#1,104:1\n37#2,2:105\n*S KotlinDebug\n*F\n+ 1 BaseSecurityConfig.kt\ncom/service/boot/web/BaseSecurityConfig$Companion\n*L\n101#1:105,2\n*E\n"})
    /* loaded from: kapai-security-common.jar:com/service/boot/web/BaseSecurityConfig$Companion.class */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final String[] getStringArray(@NotNull Environment environment, @NotNull String key) {
            int index = 0;
            List array = new ArrayList();
            while (true) {
                String value = environment.getProperty(key + "[" + index + "]");
                if (value != null) {
                    array.add(value);
                    index++;
                } else {
                    List $this$toTypedArray$iv = array;
                    return (String[]) $this$toTypedArray$iv.toArray(new String[0]);
                }
            }
        }
    }
}
