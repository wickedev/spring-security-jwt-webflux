package io.github.wickedev.spring.reactive.security.jwt

import reactor.core.publisher.Mono

interface ReactiveJwtAuthenticationService {

    fun signIn(username: String, password: String): Mono<AuthResponse?>

    fun refresh(token: String?): Mono<AuthResponse?>
}