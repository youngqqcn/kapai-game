package com.service.boot.configuration;

import com.service.boot.converter.MappingJackson2HttpMessageConverter;
import com.service.boot.converter.enums.EnumCodeConverterFactory;
import com.service.boot.converter.enums.EnumIntegerConverterFactory;
import com.service.boot.converter.enums.EnumStringConverterFactory;
import com.service.boot.i18n.I18nMessage;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/* compiled from: SpringWebMvcConfigurer.kt */
@EnableConfigurationProperties({WebProperties.class, WebMvcProperties.class})
@Configuration
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\b\u0017\u0018��2\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0013H\u0016J\u001a\u0010\u0014\u001a\u00020\u000f2\u0010\u0010\u0015\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00170\u0016H\u0016J\u0010\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0017J\n\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0017R\u0016\u0010\u0007\u001a\n \t*\u0004\u0018\u00010\b0\bX\u0092\u0004¢\u0006\u0002\n��R\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004¢\u0006\b\n��\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004¢\u0006\b\n��\u001a\u0004\b\f\u0010\r¨\u0006\u001e"}, d2 = {"Lcom/service/boot/configuration/SpringWebMvcConfigurer;", "Lorg/springframework/web/servlet/config/annotation/WebMvcConfigurer;", "webProperties", "Lorg/springframework/boot/autoconfigure/web/WebProperties;", "webMvcProperties", "Lorg/springframework/boot/autoconfigure/web/servlet/WebMvcProperties;", "(Lorg/springframework/boot/autoconfigure/web/WebProperties;Lorg/springframework/boot/autoconfigure/web/servlet/WebMvcProperties;)V", "logger", "Lorg/slf4j/Logger;", "kotlin.jvm.PlatformType", "getWebMvcProperties", "()Lorg/springframework/boot/autoconfigure/web/servlet/WebMvcProperties;", "getWebProperties", "()Lorg/springframework/boot/autoconfigure/web/WebProperties;", "addFormatters", "", "registry", "Lorg/springframework/format/FormatterRegistry;", "addResourceHandlers", "Lorg/springframework/web/servlet/config/annotation/ResourceHandlerRegistry;", "configureMessageConverters", "converters", "", "Lorg/springframework/http/converter/HttpMessageConverter;", "configureViewResolvers", "Lorg/springframework/web/servlet/config/annotation/ViewResolverRegistry;", "coreI18nMessage", "Lcom/service/boot/i18n/I18nMessage;", "getValidator", "Lorg/springframework/validation/Validator;", "core"})
@EnableWebMvc
@Order(Integer.MIN_VALUE)
/* loaded from: kapai-core.jar:com/service/boot/configuration/SpringWebMvcConfigurer.class */
public class SpringWebMvcConfigurer implements WebMvcConfigurer {
    @NotNull
    private final WebProperties webProperties;
    @NotNull
    private final WebMvcProperties webMvcProperties;
    private final Logger logger = LoggerFactory.getLogger(SpringWebMvcConfigurer.class);

    public SpringWebMvcConfigurer(@NotNull WebProperties webProperties, @NotNull WebMvcProperties webMvcProperties) {
        this.webProperties = webProperties;
        this.webMvcProperties = webMvcProperties;
    }

    @NotNull
    public WebProperties getWebProperties() {
        return this.webProperties;
    }

    @NotNull
    public WebMvcProperties getWebMvcProperties() {
        return this.webMvcProperties;
    }

    public void configureMessageConverters(@NotNull List<HttpMessageConverter<?>> converters) {
        this.logger.info("添加消息转换器");
        converters.add(new StringHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    public void addFormatters(@NotNull FormatterRegistry registry) {
        this.logger.info("添加枚举类型转换器");
        registry.addConverterFactory(new EnumStringConverterFactory());
        registry.addConverterFactory(new EnumIntegerConverterFactory());
        registry.addConverterFactory(new EnumCodeConverterFactory());
    }

    public void configureViewResolvers(@NotNull ViewResolverRegistry registry) {
        this.logger.info("视图资源处理");
        registry.viewResolver(new InternalResourceViewResolver());
    }

    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        this.logger.info("静态资源处理");
        ResourceHandlerRegistration addResourceHandler = registry.addResourceHandler(new String[]{getWebMvcProperties().getStaticPathPattern()});
        String[] staticLocations = getWebProperties().getResources().getStaticLocations();
        addResourceHandler.addResourceLocations((String[]) Arrays.copyOf(staticLocations, staticLocations.length));
    }

    @Bean
    @Primary
    @Nullable
    public Validator getValidator() {
        MessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        resourceBundleMessageSource.addBasenames(new String[]{"i18n.validator"});
        resourceBundleMessageSource.addBasenames(new String[]{"i18n.base_validator"});
        resourceBundleMessageSource.setDefaultLocale(Locale.ENGLISH);
        resourceBundleMessageSource.setUseCodeAsDefaultMessage(false);
        resourceBundleMessageSource.setFallbackToSystemLocale(false);
        Validator localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
        localValidatorFactoryBean.setValidationMessageSource(resourceBundleMessageSource);
        return localValidatorFactoryBean;
    }

    @Bean
    @NotNull
    public I18nMessage coreI18nMessage() {
        MessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        resourceBundleMessageSource.addBasenames(new String[]{"i18n.core"});
        resourceBundleMessageSource.addBasenames(new String[]{"i18n.validator"});
        resourceBundleMessageSource.addBasenames(new String[]{"i18n.base_validator"});
        resourceBundleMessageSource.setDefaultLocale(Locale.ENGLISH);
        return new I18nMessage(resourceBundleMessageSource);
    }
}
