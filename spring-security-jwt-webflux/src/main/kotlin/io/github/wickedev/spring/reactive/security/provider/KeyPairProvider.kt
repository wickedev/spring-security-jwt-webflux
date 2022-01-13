package io.github.wickedev.spring.reactive.security.provider

import io.github.wickedev.spring.reactive.security.property.JwtProperties
import org.bouncycastle.util.io.pem.PemReader
import org.springframework.core.io.Resource
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

class KeyPairProvider(private val jwtProperties: JwtProperties) {

    companion object {
        // Recommended+ https://datatracker.ietf.org/doc/html/rfc7518#section-3.1
        const val DEFAULT_CURVE = "P-256"
    }

    fun keyPair(): KeyPair {
        if (jwtProperties.algorithm.isNotEmpty() && jwtProperties.privateKey != null && jwtProperties.publicKey != null) {
            return generateKeyPairFromProperties()
        }

        return generateKeyPair()
    }

    private fun generateKeyPairFromProperties(): KeyPair {
        val kf: KeyFactory = KeyFactory.getInstance(jwtProperties.algorithm)
        val publicContent = getResourceFileAsPemContent(jwtProperties.publicKey)
        val privateContent = getResourceFileAsPemContent(jwtProperties.privateKey)
        val publicKey = kf.generatePublic(X509EncodedKeySpec(publicContent))
        val privateKey = kf.generatePrivate(PKCS8EncodedKeySpec(privateContent))
        return KeyPair(publicKey, privateKey)
    }

    private fun generateKeyPair(): KeyPair {
        val ecGenParameterSpec = ECGenParameterSpec(DEFAULT_CURVE)
        val keyPairGenerator = KeyPairGenerator.getInstance("ECDSA")
        keyPairGenerator.initialize(ecGenParameterSpec)
        return keyPairGenerator.generateKeyPair()
    }

    private fun getResourceFileAsPemContent(resource: Resource?): ByteArray? {
        return resource?.let { PemReader(it.inputStream.reader()).readPemObject().content }
    }
}