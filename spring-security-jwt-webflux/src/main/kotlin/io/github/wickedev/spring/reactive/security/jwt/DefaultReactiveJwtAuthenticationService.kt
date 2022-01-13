@file:Suppress("unused", "UNUSED_VARIABLE")

package io.github.wickedev.spring.reactive.security.jwt

import io.github.wickedev.spring.reactive.security.decoder.JwtDecoder
import io.github.wickedev.spring.reactive.security.decoder.TokenType
import io.github.wickedev.spring.reactive.security.encoder.JwtEncoder
import io.github.wickedev.spring.reactive.security.property.JwtProperties
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono

class DefaultReactiveJwtAuthenticationService(
    private val jwtProperties: JwtProperties,
    private val jwtEncoder: JwtEncoder,
    private val jwtDecoder: JwtDecoder,
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: ReactiveUserDetailsService,
) : ReactiveJwtAuthenticationService {

    override fun signIn(username: String, password: String): Mono<AuthResponse?> = mono {
        val user = userDetailsService.findByUsername(username).awaitFirstOrNull()
            ?: return@mono null

        if (!passwordEncoder.matches(password, user.password)) {
            return@mono null
        }

        val accessToken = jwtEncoder.encode(user, TokenType.Access)
        val refreshToken = jwtEncoder.encode(user, TokenType.Refresh)

        return@mono AuthResponse(
            accessToken = accessToken.value,
            expiresIn = accessToken.expiresIn,
            refreshToken = refreshToken.value,
            refreshExpiresIn = refreshToken.expiresIn,
            scope = authoritiesToScope(user.authorities)
        )
    }

    override fun refresh(token: String?): Mono<AuthResponse?> = mono {
        if (token == null) {
            return@mono null
        }

        val jwt = jwtDecoder.decode(token)
            ?: return@mono null

        if (jwt.type != TokenType.Refresh) {
            return@mono null
        }

        val user = userDetailsService.findByUsername(jwt.subject).awaitFirstOrNull()
            ?: return@mono null

        val accessToken = jwtEncoder.encode(user, TokenType.Access)
        val refreshToken = jwtEncoder.encode(user, TokenType.Refresh)

        return@mono AuthResponse(
            accessToken = accessToken.value,
            expiresIn = accessToken.expiresIn,
            refreshToken = refreshToken.value,
            refreshExpiresIn = refreshToken.expiresIn,
            scope = authoritiesToScope(jwt.authorities)
        )
    }

    private fun authoritiesToScope(authorities: Collection<GrantedAuthority>): String {
        return authorities.map { it.authority }.joinToString(",")
    }
}