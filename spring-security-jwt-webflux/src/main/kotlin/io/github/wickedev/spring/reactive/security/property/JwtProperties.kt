package io.github.wickedev.spring.reactive.security.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.core.io.Resource
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties("security.jwt")
class JwtProperties(
    val algorithm: String = "EC",
    val issuer: String = "",
    val privateKey: Resource? = null,
    val publicKey: Resource? = null,
    val accessTokenExpiresIn: Duration = Duration.ofHours(10),
    val refreshTokenExpiresIn: Duration = Duration.ofDays(600),
)