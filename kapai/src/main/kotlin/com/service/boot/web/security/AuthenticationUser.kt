package com.service.boot.web.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

open class AuthenticationUser(val principal: Any, val authorities: Set<GrantedAuthority> = emptySet(), val permissions: Map<String, String> = emptyMap()) {

    companion object {
        @JvmStatic
        fun getAuthenticationUser(): AuthenticationUser? {
            return (SecurityContextHolder.getContext().authentication as? AuthenticationToken)?.details as? AuthenticationUser
        }

    }
}