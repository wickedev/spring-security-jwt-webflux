plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.6.10"
    id("org.springframework.boot") version "2.6.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    api("org.springframework.security:spring-security-core")
    api("org.springframework.security:spring-security-web")
    api("org.springframework.boot:spring-boot-starter-webflux")
    api("org.springframework.security:spring-security-oauth2-jose")
    api("org.bouncycastle:bcpkix-jdk15on:1.69")
    api("com.nimbusds:nimbus-jose-jwt:9.19")

    /* testing */
    testImplementation("io.kotest:kotest-runner-junit5:5.1.0")
    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.0")

    testImplementation(project(":spring-security-jwt-webflux-autoconfigure"))
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
