package io.github.wickedev.spring.reactive.security.jwt

import org.springframework.security.core.userdetails.UserDetails

data class AuthResponse(
    val user: UserDetails,
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val scope: String,
    val refreshToken: String,
    val refreshExpiresIn: Long,
)
