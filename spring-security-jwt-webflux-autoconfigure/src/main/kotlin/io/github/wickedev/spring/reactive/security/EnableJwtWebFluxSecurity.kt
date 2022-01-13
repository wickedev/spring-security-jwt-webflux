package io.github.wickedev.spring.reactive.security

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@MustBeDocumented
@Import(
    JwtWebFluxSecurityConfiguration::class,
)
@Configuration
annotation class EnableJwtWebFluxSecurity