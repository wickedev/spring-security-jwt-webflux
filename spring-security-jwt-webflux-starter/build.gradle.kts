plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.6.10"
    id("org.springframework.boot") version "2.6.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":spring-security-jwt-webflux-autoconfigure"))
    api("org.springframework.boot:spring-boot-starter-security")
}
