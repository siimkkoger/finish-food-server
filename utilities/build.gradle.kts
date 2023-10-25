import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "com.ffreaky"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    val queryDslVersion = "5.0.0"
    val lombokVersion = "1.18.28"

    implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")

    // Database connectivity
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa", version = "3.0.4")

    // Querydsl
    implementation(group = "com.querydsl", name = "querydsl-core", version = queryDslVersion)
    implementation(group = "com.querydsl", name = "querydsl-jpa", version = queryDslVersion)
    implementation(group = "com.querydsl", name = "querydsl-apt", version = queryDslVersion, classifier = "jakarta")

    // Test
    testImplementation(group = "org.springframework.boot", name = "spring-boot-starter-test", version = "3.1.0")

    // Validation
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-validation", version = "3.1.3")

    // Lombok
    compileOnly(group = "org.projectlombok", name = "lombok", version = lombokVersion)
    testCompileOnly(group = "org.projectlombok", name = "lombok", version = lombokVersion)

    // Annotation processor dependencies
    annotationProcessor(group = "com.querydsl", name = "querydsl-apt", version = queryDslVersion, classifier = "jakarta")
    annotationProcessor(group = "jakarta.persistence", name = "jakarta.persistence-api", version = "3.1.0")
    annotationProcessor(group = "jakarta.annotation", name = "jakarta.annotation-api", version = "2.1.1")
    annotationProcessor(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa", version = "3.1.0")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = lombokVersion)

    testAnnotationProcessor(group = "org.projectlombok", name = "lombok", version = lombokVersion)
    testAnnotationProcessor(group = "com.querydsl", name = "querydsl-apt", version = queryDslVersion, classifier = "jakarta")
}

tasks.register("prepareKotlinBuildScriptModel") {}

tasks.withType<Jar> {
    enabled = true
    archiveBaseName.set("utilities")
}

tasks.withType<BootJar> {
    enabled = false
}
