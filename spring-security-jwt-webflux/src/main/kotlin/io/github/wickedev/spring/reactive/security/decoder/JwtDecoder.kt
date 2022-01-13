@file:Suppress("unused")

package io.github.wickedev.spring.reactive.security.decoder

interface JwtDecoder {
    fun decode(token: String?): JWTUserDetails?
}

