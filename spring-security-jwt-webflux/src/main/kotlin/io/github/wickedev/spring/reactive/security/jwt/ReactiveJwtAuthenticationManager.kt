package io.github.wickedev.spring.reactive.security.jwt

import io.github.wickedev.spring.reactive.security.BearerTokenAuthenticationToken
import io.github.wickedev.spring.reactive.security.decoder.JwtDecoder
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono

class ReactiveJwtAuthenticationManager(private val jwtDecoder: JwtDecoder) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> = mono {

        if (authentication !is BearerTokenAuthenticationToken) {
            return@mono authentication
        }

        val jwt = jwtDecoder.decode(authentication.token)
            ?: throw BadCredentialsException("Invalid Credentials")

        return@mono JwtAuthenticationToken(jwt)
    }
}