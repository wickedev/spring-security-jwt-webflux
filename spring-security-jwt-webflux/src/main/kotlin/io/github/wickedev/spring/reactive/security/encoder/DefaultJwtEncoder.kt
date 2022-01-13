package io.github.wickedev.spring.reactive.security.encoder

import io.github.wickedev.spring.reactive.security.IdentifiableUserDetails
import io.github.wickedev.spring.reactive.security.decoder.TokenType
import io.github.wickedev.spring.reactive.security.jwt.Token
import io.github.wickedev.spring.reactive.security.property.JwtProperties
import io.github.wickedev.spring.reactive.security.provider.JWKProvider
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.security.Security
import java.time.Duration
import java.time.Instant


class DefaultJwtEncoder(
    private val jwtProperties: JwtProperties,
    jwkProvider: JWKProvider,
) : JwtEncoder {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    private val encoder = NimbusJwtEncoder(jwkProvider.jwkSource())
    private val jwsHeader: JwsHeader = JwsHeader.with(SignatureAlgorithm.ES256).build()

    override fun encode(userDetails: UserDetails, type: TokenType): Token {
        val jwtClaimsSet: JwtClaimsSet = jwtClaimsSet(userDetails, type).build()
        val encoded = encoder.encode(JwtEncoderParameters.from(jwsHeader, jwtClaimsSet))

        return Token(
            value = encoded.tokenValue,
            expiresIn = encoded.expiresAt?.epochSecond ?: 0
        )
    }

    private fun jwtClaimsSet(
        userDetails: UserDetails, type: TokenType
    ): JwtClaimsSet.Builder {
        val id = if(userDetails is IdentifiableUserDetails) userDetails.getIdentifier() else null
        val issuer = jwtProperties.issuer
        val issuedAt: Instant = Instant.now()
        val expiresIn: Duration = if (type == TokenType.Refresh)
            jwtProperties.refreshTokenExpiresIn
        else
            jwtProperties.accessTokenExpiresIn
        val expiresAt: Instant = issuedAt.plus(expiresIn)

        return JwtClaimsSet.builder()
            .subject(userDetails.username)
            .issuer(issuer)
            .issuedAt(issuedAt)
            .notBefore(issuedAt)
            .expiresAt(expiresAt)
            .claim("id", id)
            .claim("type", type)
            .claim("roles", userDetails.authorities.map { it.authority })
    }
}