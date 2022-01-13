package io.github.wickedev.spring.reactive.security

import org.springframework.security.authentication.AbstractAuthenticationToken

data class BearerTokenAuthenticationToken(val token: String): AbstractAuthenticationToken(emptyList()) {

    override fun getCredentials(): Any = token

    override fun getPrincipal(): Any = token
}