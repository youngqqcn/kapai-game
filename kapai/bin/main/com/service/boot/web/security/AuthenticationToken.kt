package com.service.boot.web.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AuthenticationToken : UsernamePasswordAuthenticationToken {

    private var params: Map<String, Any?>? = null
    private var request: HttpServletRequest? = null

    constructor(principal: Any?, authorities: Collection<GrantedAuthority>, detail: Any?) : super(principal, null, authorities) {
        this.details = detail
    }

    constructor(request: HttpServletRequest, params: Map<String, Any?>) : super(null, null) {
        this.request = request
        this.params = params
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(javaClass.simpleName).append(" [")
        sb.append("Principal=").append(principal).append(", ")
        sb.append("Credentials=[PROTECTED], ")
        sb.append("Authenticated=").append(isAuthenticated).append(", ")
        sb.append("Details=").append(details).append(", ")
        sb.append("Granted Authorities=").append(authorities).append(", ")
        sb.append("Params=").append(params)
        sb.append("]")
        return sb.toString()
    }

    fun getAuthParams() = params.orEmpty()

    fun getHttpServletRequest() = request

}