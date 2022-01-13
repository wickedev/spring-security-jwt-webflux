@file:Suppress("unused")

package io.github.wickedev.spring.reactive.security.encoder

import io.github.wickedev.spring.reactive.security.decoder.TokenType
import io.github.wickedev.spring.reactive.security.jwt.Token
import org.springframework.security.core.userdetails.UserDetails

interface JwtEncoder {
    fun encode(userDetails: UserDetails, type: TokenType): Token
}


