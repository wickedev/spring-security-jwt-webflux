package io.github.wickedev.spring.reactive.security.decoder

import io.github.wickedev.spring.reactive.security.IdentifiableUserDetails

interface JWTUserDetails: IdentifiableUserDetails {

    val subject: String

    val type: TokenType
}
