package io.github.wickedev.spring.reactive.security.jwt

data class Token(
    val value: String,
    val expiresIn: Long
)