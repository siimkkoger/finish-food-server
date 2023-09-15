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
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
