package io.github.wickedev.spring.reactive.security

import io.github.wickedev.spring.reactive.security.property.JwtProperties
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import java.time.Duration

@SpringBootTest
class JwtPropertiesTest(
    private val jwtProperties: JwtProperties
) : DescribeSpec({
    describe("JwtProperties") {
        it("should be") {
            jwtProperties.algorithm shouldBe "EC"
            jwtProperties.issuer shouldBe "https://graphql-jetpack.github.io"
            jwtProperties.privateKey shouldBe ClassPathResource("keys/ec256-private.pem")
            jwtProperties.publicKey shouldBe ClassPathResource("keys/ec256-public.pem")
            jwtProperties.accessTokenExpiresIn shouldBe Duration.ofHours(1)
            jwtProperties.refreshTokenExpiresIn shouldBe Duration.ofDays(60)
        }
    }
})
