package com.service.boot.web.security

import com.service.boot.json.JSON
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpMethod
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class VerificationAuthenticationFilter : UsernamePasswordAuthenticationFilter {

    constructor()
    constructor(filterProcessesUrl: String) {
        setFilterProcessesUrl(filterProcessesUrl)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        if (!HttpMethod.POST.matches(request.method)) throw AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        val contentType = MediaType.parseMediaType(request.contentType)
        return if (contentType.includes(MediaType.APPLICATION_FORM_URLENCODED) || contentType.includes(MediaType.MULTIPART_FORM_DATA)) {
            val params = mutableMapOf<String, Any?>()
            val keys = request.parameterNames
            while (keys.hasMoreElements()) {
                val key = keys.nextElement()
                params[key] = request.getParameter(key)
            }
            authenticationManager.authenticate(AuthenticationToken(request, params))
        } else if (contentType.includes(MediaType.APPLICATION_JSON)) {
            val json = request.inputStream.bufferedReader().readText()
            val params = JSON.parseObject(json) ?: throw AuthenticationServiceException("Authentication json content fail: $json")
            authenticationManager.authenticate(AuthenticationToken(request, params))
        } else throw AuthenticationServiceException("Authentication content type not supported: ${request.contentType}")
    }
}