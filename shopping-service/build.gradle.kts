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
    val queryDslVersion = "5.0.0"
    val lombokVersion = "1.18.28"

    implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")

    // Database connectivity
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa", version = "3.0.4")
    implementation(group = "org.jooq", name = "jooq", version = "3.18.0")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-redis")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-cache")

    // Querydsl
    implementation(group = "com.querydsl", name = "querydsl-core", version = queryDslVersion)
    implementation(group = "com.querydsl", name = "querydsl-jpa", version = queryDslVersion, classifier = "jakarta")
    implementation(group = "com.querydsl", name = "querydsl-apt", version = queryDslVersion, classifier = "jakarta")

    // Postgresql
    runtimeOnly(group = "org.postgresql", name = "postgresql", version = "42.5.4")

    // Test
    testImplementation(group = "org.springframework.boot", name = "spring-boot-starter-test", version = "3.1.0")

    // OpenAPI (Swagger)
    implementation(group = "org.springdoc", name = "springdoc-openapi-starter-webmvc-ui", version = "2.0.2")

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

    // Utilities module
    implementation(project(":utilities"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    enabled = false
}

tasks.withType<BootJar> {
    enabled = true
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

/*
val generatedQueryDslDirName = "generatedQueryDsl"

tasks.register("generateQueryDSL", JavaCompile::class) {
    val generatedDir = file(generatedQueryDslDirName)
    source = sourceSets.main.get().java
    classpath = configurations.compileClasspath.get() + configurations.annotationProcessor.get()
    options.compilerArgs = listOf(
        "-proc:only",
        "-processor", "com.querydsl.apt.jpa.JPAAnnotationProcessor"
    )
    getDestinationDirectory().set(generatedDir)
}

tasks.named("compileJava") {
    dependsOn("generateQueryDSL")
    //source(sourceSets.getByName(generatedQueryDslDirName).java.srcDirs.iterator().next())
}

tasks.named("clean") {
    doLast {
        val generatedDir = file(generatedQueryDslDirName)
        generatedDir.deleteRecursively()
    }
}
 */

/*
idea {
    module {
        sourceDirs += file("generated")
        generatedSourceDirs += file("generated/")
    }
}
 */