package io.github.wickedev.spring.reactive.security.provider

import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory
import com.nimbusds.jose.jwk.*
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import java.security.PrivateKey
import java.security.PublicKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPublicKey

class JWKProvider(keyPairProvider: KeyPairProvider) {
    private val jwsVerifierFactory = DefaultJWSVerifierFactory()
    private val keyPair = keyPairProvider.keyPair()

    fun jwkSource(): JWKSource<SecurityContext> {

        return ImmutableJWKSet(JWKSet(listOf(jwk(keyPair.public, keyPair.private))))
    }

    fun verifier(header: JWSHeader): JWSVerifier {
        return jwsVerifierFactory.createJWSVerifier(header, keyPair.public)
    }

    private fun jwk(publicKey: PublicKey, privateKey: PrivateKey): JWK {
        return when (publicKey) {
            is ECPublicKey -> {
                val curve: Curve = Curve.forECParameterSpec(publicKey.params)
                ECKey.Builder(curve, publicKey)
                    .privateKey(privateKey)
                    .keyID("ec-jwk-kid")
                    .build()
            }
            is RSAPublicKey -> {
                RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID("rsa-jwk-kid")
                    .build()

            }
            else -> {
                throw UnsupportedAlgorithmKeyException()
            }
        }
    }
}