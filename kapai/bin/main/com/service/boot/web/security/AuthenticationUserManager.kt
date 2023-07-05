package com.service.boot.web.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication

class AuthenticationUserManager(private val service: AuthenticationUserService?) : AuthenticationManager {

    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication as AuthenticationToken
        val user = service?.onAuthentication(token) ?: throw BadCredentialsException("登录认证失败")
        return AuthenticationToken(user.principal, user.authorities, user)
    }
}