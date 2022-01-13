package io.github.wickedev.spring.reactive.security.decoder

import com.nimbusds.jwt.SignedJWT
import org.springframework.security.core.authority.SimpleGrantedAuthority

class DefaultJwtDecoder(private val jwtVerifier: JwtVerifier) : JwtDecoder {
    override fun decode(token: String?): JWTUserDetails? {
        if (token == null) {
            return null
        }
        val jwt = SignedJWT.parse(token)

        if (!jwtVerifier.verify(jwt)) {
            return null
        }


        return DefaultJWTUserDetails(
            id = jwt.jwtClaimsSet.getClaim("id"),
            subject = jwt.jwtClaimsSet.subject,
            type = TokenType.valueOf(jwt.jwtClaimsSet.getStringClaim("type")),
            authorities = jwt.jwtClaimsSet.getStringArrayClaim("roles").map { SimpleGrantedAuthority(it) }
        )
    }
}



