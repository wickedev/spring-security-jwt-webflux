package io.github.wickedev.spring.reactive.security.jwt

import io.github.wickedev.spring.reactive.security.decoder.JWTUserDetails
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.util.Assert

class JwtAuthenticationToken(private val jwtUserDetails: JWTUserDetails) : Authentication {

    fun getId(): Any = jwtUserDetails.getIdentifier()

    override fun getName(): String = jwtUserDetails.subject

    override fun getAuthorities(): Collection<GrantedAuthority> = jwtUserDetails.authorities

    override fun getCredentials(): String? = null

    override fun getDetails(): JWTUserDetails = jwtUserDetails

    override fun getPrincipal(): JWTUserDetails = jwtUserDetails

    override fun isAuthenticated(): Boolean = true

    @Throws(IllegalArgumentException::class)
    override fun setAuthenticated(isAuthenticated: Boolean) {
        Assert.isTrue(
            isAuthenticated,
            "Cannot set this token to untrusted - use constructor which takes a Claims instead"
        )
    }
}