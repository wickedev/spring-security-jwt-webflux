package io.github.wickedev.spring.reactive.security.decoder

import org.springframework.security.core.GrantedAuthority

data class DefaultJWTUserDetails(
    val id: Any,
    override val subject: String,
    override val type: TokenType,
    @JvmField val authorities: Collection<GrantedAuthority>,
) : JWTUserDetails {
    override fun getIdentifier(): Any = id

    override fun getUsername(): String = subject

    override fun getPassword(): String? = null

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}