package io.github.wickedev.spring.reactive.security

import io.kotest.core.spec.style.DescribeSpec
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@SpringBootTest
@AutoConfigureWebTestClient
class JwtTest(
    private val webTestClient: WebTestClient
) : DescribeSpec({
    describe("JWT") {
        it("should be login return auth response") {
            webTestClient.post()
                .uri("/login")
                .body(BodyInserters.fromValue(AuthRequest("test@test.com", "password")))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.user").hasJsonPath()
                .jsonPath("$.accessToken").hasJsonPath()
                .jsonPath("$.tokenType").isEqualTo("Bearer")
                .jsonPath("$.expiresIn").isNumber
                .jsonPath("$.scope").hasJsonPath()
                .jsonPath("$.refreshToken").hasJsonPath()
                .jsonPath("$.refreshExpiresIn").isNumber
        }

        it("should be refresh return auth response") {
            val token =
                "eyJraWQiOiJlYy1qd2sta2lkIiwiYWxnIjoiRVMyNTYifQ.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwibmJmIjowLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoiaHR0cHM6XC9cL2dyYXBocWwtamV0cGFjay5naXRodWIuaW8iLCJpZCI6MSwiZXhwIjoyMTQ3NDgzNjQ3LCJ0eXBlIjoiUmVmcmVzaCIsImlhdCI6MH0.Ln0Zey0OcJQm25wq1ifCc7foC--A-8DTkIf4uHVNoaec5JLm-1Pa2eOFuKQri2m-gSn7Tm5XbmXFgI2wfm4STg"
            webTestClient.post()
                .uri("/refresh")
                .body(BodyInserters.fromValue(token))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.user").hasJsonPath()
                .jsonPath("$.accessToken").hasJsonPath()
                .jsonPath("$.tokenType").isEqualTo("Bearer")
                .jsonPath("$.expiresIn").isNumber
                .jsonPath("$.scope").hasJsonPath()
                .jsonPath("$.refreshToken").hasJsonPath()
                .jsonPath("$.refreshExpiresIn").isNumber
        }

        it("should protected endpoint deny without token") {
            webTestClient.get()
                .uri("/protect/user")
                .exchange()
                .expectStatus().isUnauthorized
                .expectBody()
                .consumeWith(System.out::println)
        }

        it("should protected endpoint deny with expired JWT token") {
            val token =
                "eyJraWQiOiJlYy1qd2sta2lkIiwiYWxnIjoiRVMyNTYifQ.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwibmJmIjowLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoiaHR0cHM6XC9cL2dyYXBocWwtamV0cGFjay5naXRodWIuaW8iLCJpZCI6MSwiZXhwIjoxLCJ0eXBlIjoiQWNjZXNzIiwiaWF0IjowfQ.SjzelZ81iZKdJtP1SKQsfkpheYoStvRaFPfjJ5fq7UIr9qE0Vd1xP9ijPPYC5IJUK2ZjTusp5jugqAyBiZor5A"

            webTestClient.get()
                .uri("/protect/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .exchange()
                .expectStatus().isUnauthorized
                .expectBody()
                .consumeWith(System.out::println)
        }

        it("should protected endpoint allow with valid JWT token") {
            val token =
                "eyJraWQiOiJlYy1qd2sta2lkIiwiYWxnIjoiRVMyNTYifQ.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwibmJmIjowLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoiaHR0cHM6XC9cL2dyYXBocWwtamV0cGFjay5naXRodWIuaW8iLCJpZCI6MSwiZXhwIjoyMTQ3NDgzNjQ3LCJ0eXBlIjoiQWNjZXNzIiwiaWF0IjowfQ.Tshg9prs1nlxqfYOzLSvwfkjiGxEaa8iEF9BEJb_OmjYexNJ1GFgBtpMn0d2vA9tFy00RB4dsL7zK6maZAXQNQ"

            webTestClient.get()
                .uri("/protect/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith(System.out::println)
        }
    }
})
