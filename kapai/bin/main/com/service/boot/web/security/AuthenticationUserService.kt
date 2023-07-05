package com.service.boot.web.security

abstract class AuthenticationUserService {
    abstract fun onAuthentication(token: AuthenticationToken): AuthenticationUser?

    abstract fun createAuthenticationInfo(user: AuthenticationUser?): Any?

    open fun onLogout(user: AuthenticationUser?): Boolean {
        return true
    }
}