import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"
}

group = "pl.kossa"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    //Firebase
    implementation("com.google.firebase:firebase-admin:8.2.0")

    //Spring
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter-security:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.0")

    //Validation
    implementation("javax.validation:validation-api:2.0.1.Final")

    //Mongo
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.7.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:2.7.0")

    //Swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.6.9")
    implementation("org.springdoc:springdoc-openapi-data-rest:1.6.9")
    implementation("org.springdoc:springdoc-openapi-security:1.6.9")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.9")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")

    //Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.2")


    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.0") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    //Images
    implementation("org.imgscalr:imgscalr-lib:4.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
