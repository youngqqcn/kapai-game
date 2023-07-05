package com.service.boot.web

import com.service.boot.web.security.AuthenticationHandler
import com.service.boot.web.security.AuthenticationUserManager
import com.service.boot.web.security.AuthenticationUserService
import com.service.boot.web.security.VerificationAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.web.servlet.LocaleResolver

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SpringSecurityConfig : BaseSecurityConfig() {

    @Autowired(required = false)
    private var service: AuthenticationUserService? = null

    @Bean
    @Order(ORDER_SECURITY_DEFAULT)
    fun securityFilterChain(
        http: HttpSecurity,
        environment: Environment,
        localeResolver: LocaleResolver,
        i18nMessageSource: MessageSource
    ): SecurityFilterChain {
        http.cors(Customizer.withDefaults())
        http.csrf { it.disable() }
        http.httpBasic { it.disable() }
        http.getConfigurer(DefaultLoginPageConfigurer<HttpSecurity>()::class.java).disable()

        val authenticationManager = AuthenticationUserManager(service)
        http.authenticationManager(authenticationManager)

        val authenticationHandler = AuthenticationHandler(localeResolver, i18nMessageSource, service)

        http.formLogin {
            val method = it.javaClass.superclass.getDeclaredMethod("setAuthenticationFilter", AbstractAuthenticationProcessingFilter::class.java)
            method.isAccessible = true
            method.invoke(it, VerificationAuthenticationFilter())
            it.loginProcessingUrl(LOGIN_PATH)
            it.successHandler(authenticationHandler)
            it.failureHandler(authenticationHandler)
        }
        http.logout {
            it.logoutUrl(LOGOUT_PATH).logoutSuccessHandler(authenticationHandler).addLogoutHandler(authenticationHandler)
        }
        http.sessionManagement {
            it.maximumSessions(1)
        }
        http.exceptionHandling {
            it.authenticationEntryPoint(authenticationHandler).accessDeniedHandler(authenticationHandler)
        }

        http.authorizeHttpRequests { registry ->
            registry.requestMatchers(*EXCLUDE_WEB_IGNORING_PATH_ARRAY).permitAll()
            registry.requestMatchers(*EXCLUDE_PATH_ARRAY).permitAll()
            registry.requestMatchers(*getStringArray(environment, "spring.security.filter.exclude")).permitAll()
            registry.anyRequest().authenticated()
        }
        http.headers { headersConfigurer ->
            headersConfigurer.frameOptions { frameOptionsConfig ->
                frameOptionsConfig.disable()
            }
        }
        return http.build()
    }

}