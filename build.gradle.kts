import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.0-M1"
}

group = "pl.kossa"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    //Firebase
    implementation("com.google.firebase:firebase-admin:8.1.0")

    //Spring
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.3")
    implementation("org.springframework.boot:spring-boot-starter-security:2.6.3")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.3")

    //Validation
    implementation("javax.validation:validation-api:2.0.1.Final")

    //Mongo
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.6.3")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:2.6.3")

    //Swagger
    implementation("org.springdoc:spring-doc-openapi-ui:1.6.3")
    implementation("org.springdoc:spring-doc-openapi-data-rest:1.6.3")
    implementation("org.springdoc:spring-doc-openapi-security:1.6.3")
    implementation("org.springdoc:spring-doc-openapi-kotlin:1.6.3")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")

    //Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.0-native-mt")


    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.3") {
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
