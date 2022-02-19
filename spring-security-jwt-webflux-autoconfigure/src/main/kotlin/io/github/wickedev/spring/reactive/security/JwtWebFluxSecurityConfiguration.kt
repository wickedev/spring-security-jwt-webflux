package io.github.wickedev.spring.reactive.security

import io.github.wickedev.spring.reactive.security.decoder.DefaultJwtDecoder
import io.github.wickedev.spring.reactive.security.decoder.JwtDecoder
import io.github.wickedev.spring.reactive.security.decoder.JwtVerifier
import io.github.wickedev.spring.reactive.security.encoder.DefaultJwtEncoder
import io.github.wickedev.spring.reactive.security.encoder.JwtEncoder
import io.github.wickedev.spring.reactive.security.jwt.DefaultReactiveJwtAuthenticationService
import io.github.wickedev.spring.reactive.security.jwt.ReactiveJwtAuthenticationService
import io.github.wickedev.spring.reactive.security.property.JwtProperties
import io.github.wickedev.spring.reactive.security.provider.JWKProvider
import io.github.wickedev.spring.reactive.security.provider.KeyPairProvider
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(JwtProperties::class)
@AutoConfigureAfter(ReactiveSecurityAutoConfiguration::class)
class JwtWebFluxSecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun authenticationManager(
        userDetailsService: ReactiveUserDetailsService,
        passwordEncoder: PasswordEncoder
    ): ReactiveAuthenticationManager =
        UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService).apply {
            setPasswordEncoder(passwordEncoder)
        }

    @Bean
    @ConditionalOnMissingBean
    fun passwordEncoder(): PasswordEncoder = Argon2PasswordEncoder()

    @Bean
    @ConditionalOnMissingBean
    fun jwtAuthenticationService(
        jwtEncoder: JwtEncoder,
        jwtDecoder: JwtDecoder,
        passwordEncoder: PasswordEncoder,
        userDetailsService: ReactiveUserDetailsService,
    ): ReactiveJwtAuthenticationService {
        return DefaultReactiveJwtAuthenticationService(
            jwtEncoder,
            jwtDecoder,
            passwordEncoder,
            userDetailsService
        )
    }

    @Bean
    @ConditionalOnMissingBean
    fun keyPairProvider(jwtProperties: JwtProperties): KeyPairProvider = KeyPairProvider(jwtProperties)

    @Bean
    @ConditionalOnMissingBean
    fun jwkProvider(keyPairProvider: KeyPairProvider): JWKProvider = JWKProvider(keyPairProvider)

    @Bean
    @ConditionalOnMissingBean
    fun jwtVerifier(jwkProvider: JWKProvider): JwtVerifier = JwtVerifier(jwkProvider)

    @Bean
    @ConditionalOnMissingBean
    fun jwtEncoder(jwtProperties: JwtProperties, jwkProvider: JWKProvider): JwtEncoder =
        DefaultJwtEncoder(jwtProperties, jwkProvider)

    @Bean
    @ConditionalOnMissingBean
    fun jwtDecoder(jwtVerifier: JwtVerifier): JwtDecoder = DefaultJwtDecoder(jwtVerifier)
}