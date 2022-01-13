package io.github.wickedev.spring.reactive.security

import org.springframework.security.core.userdetails.UserDetails

interface IdentifiableUserDetails : UserDetails {
    fun getIdentifier(): Any
}