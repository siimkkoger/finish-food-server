import org.springframework.boot.gradle.tasks.bundling.BootJar

val requiredJavaVersion = JavaVersion.VERSION_17

if (JavaVersion.current() != requiredJavaVersion) {
    throw GradleException("This project requires Java ${requiredJavaVersion.majorVersion}, but it's running on ${JavaVersion.current()}")
}

plugins {
    java
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "com.ffreaky"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = requiredJavaVersion
    targetCompatibility = requiredJavaVersion
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Database connectivity
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Postgresql
    runtimeOnly("org.postgresql:postgresql")

    // Flyway
    //implementation("org.flywaydb:flyway-core")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    // OpenAPI (Swagger)
    implementation(group = "org.springdoc", name = "springdoc-openapi-starter-webmvc-ui", version = "2.0.2")

    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation:3.1.3")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    testCompileOnly("org.projectlombok:lombok:1.18.28")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.28")

    // Utilities module
    implementation(project(":utilities"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType <Jar> {
    enabled = false
}

tasks.withType<BootJar> {
    enabled = true
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
