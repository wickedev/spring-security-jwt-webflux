package io.github.wickedev.spring.reactive.security

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class BearerServerAuthenticationConverter : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication?> = mono {
        ReactiveSecurityContextHolder.getContext().awaitFirstOrNull()?.authentication
            ?: createBearerTokenAuthenticationToken(exchange)
    }

    private fun createBearerTokenAuthenticationToken(exchange: ServerWebExchange): Authentication? {
        return resolveToken(exchange.request.headers)?.let {
            io.github.wickedev.spring.reactive.security.BearerTokenAuthenticationToken(
                it
            )
        }
    }

    private fun resolveToken(headers: HttpHeaders): String? {
        val bearerToken = headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return null

        if (bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }

        return null
    }
}