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
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // OpenAPI (Swagger)
    implementation(group = "org.springdoc", name = "springdoc-openapi-starter-webmvc-ui", version = "2.0.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
