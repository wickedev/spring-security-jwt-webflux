package io.github.wickedev.spring.reactive.security.decoder

import com.nimbusds.jwt.SignedJWT
import io.github.wickedev.spring.reactive.security.provider.JWKProvider
import java.util.*

class JwtVerifier(
    private val jwkProvider: JWKProvider
) {
    fun verify(jwt: SignedJWT): Boolean {
        val now = Date()

        if (!jwt.verify(jwkProvider.verifier(jwt.header))) {
            return false
        }

        if (jwt.jwtClaimsSet.notBeforeTime > now) {
            return false
        }

        if (jwt.jwtClaimsSet.issueTime > now) {
            return false
        }

        if (now > jwt.jwtClaimsSet.expirationTime) {
            return false
        }

        return true
    }
}