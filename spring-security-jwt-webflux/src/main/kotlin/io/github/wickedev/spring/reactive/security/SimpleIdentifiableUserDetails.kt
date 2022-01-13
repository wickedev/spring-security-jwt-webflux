package io.github.wickedev.spring.reactive.security

interface SimpleIdentifiableUserDetails : IdentifiableUserDetails {

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}