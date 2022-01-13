package io.github.wickedev.spring.reactive.security.jwt

import io.github.wickedev.spring.reactive.security.BearerServerAuthenticationConverter
import io.github.wickedev.spring.reactive.security.decoder.JwtDecoder
import org.springframework.http.HttpStatus
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler

class JwtAuthenticationWebFilter(jwtDecoder: JwtDecoder) :
    AuthenticationWebFilter(ReactiveJwtAuthenticationManager(jwtDecoder)) {
    init {
        setServerAuthenticationConverter(BearerServerAuthenticationConverter())
        setAuthenticationFailureHandler(
            ServerAuthenticationEntryPointFailureHandler(
                HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)
            )
        )
    }
}
