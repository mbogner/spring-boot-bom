plugins {
    `java-library` // we don't want to create a jar but found no other working solution so far
    signing // required for maven central
    id("maven-publish")
    // https://plugins.gradle.org/plugin/io.spring.dependency-management
    id("io.spring.dependency-management") version "1.1.6"
    // https://plugins.gradle.org/plugin/io.github.gradle-nexus.publish-plugin
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    // https://plugins.gradle.org/plugin/net.researchgate.release
    id("net.researchgate.release") version "3.0.2"
}

group = "dev.mbo"

dependencyManagement {
    imports {
        // https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-parent
        mavenBom("org.springframework.cloud:spring-cloud-starter-parent:2023.0.3")
        // https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-dependencies
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
        // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-dependencies
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.5")
        // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-bom
        mavenBom("org.jetbrains.kotlin:kotlin-bom:2.0.21")
        // https://mvnrepository.com/artifact/org.testcontainers/testcontainers-bom
        mavenBom("org.testcontainers:testcontainers-bom:1.20.3")
        // https://mvnrepository.com/artifact/software.amazon.awssdk/bom
        mavenBom("software.amazon.awssdk:bom:2.29.6")
        // https://mvnrepository.com/artifact/io.ktor/ktor-bom
        mavenBom("io.ktor:ktor-bom:3.0.1")
    }
    dependencies {
        // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
        dependency("org.apache.commons:commons-lang3:3.17.0")
        // https://mvnrepository.com/artifact/commons-io/commons-io
        dependency("commons-io:commons-io:2.17.0")
        // https://mvnrepository.com/artifact/org.apache.commons/commons-pool2
        dependency("org.apache.commons:commons-pool2:2.12.0")
        // https://mvnrepository.com/artifact/org.freemarker/freemarker
        dependency("org.freemarker:freemarker:2.3.33")
        // https://mvnrepository.com/artifact/org.keycloak/keycloak-admin-client
        dependency("org.keycloak:keycloak-admin-client:26.0.2")
        // https://mvnrepository.com/artifact/com.redis/testcontainers-redis
        dependency("com.redis:testcontainers-redis:2.2.2")
        // https://mvnrepository.com/artifact/org.mockito.kotlin/mockito-kotlin
        dependency("org.mockito.kotlin:mockito-kotlin:5.4.0")
        // https://mvnrepository.com/artifact/com.aallam.openai/openai-client
        dependency("com.aallam.openai:openai-client:3.8.2")
        // https://mvnrepository.com/artifact/org.jsoup/jsoup
        dependency("org.jsoup:jsoup:1.18.1")
        // https://mvnrepository.com/artifact/org.wiremock/wiremock
        dependency("org.wiremock:wiremock:3.9.2")

        // OTP - https://mvnrepository.com/artifact/com.eatthepath/java-otp
        dependency("com.eatthepath:java-otp:0.4.0")
        // QR-Code - https://mvnrepository.com/artifact/com.google.zxing/core
        dependency("com.google.zxing:core:3.5.3")
        // https://mvnrepository.com/artifact/com.google.zxing/javase
        dependency("com.google.zxing:javase:3.5.3")

        // http client
        // https://mvnrepository.com/artifact/org.apache.httpcomponents.core5/httpcore5
        dependency("org.apache.httpcomponents.core5:httpcore5:5.3.1")
        // https://mvnrepository.com/artifact/org.apache.httpcomponents.client5/httpclient5
        dependency("org.apache.httpcomponents.client5:httpclient5:5.4.1")

        // database
        // https://mvnrepository.com/artifact/org.postgresql/postgresql
        dependency("org.postgresql:postgresql:42.7.4")
        // https://mvnrepository.com/artifact/org.flywaydb/flyway-core
        dependency("org.flywaydb:flyway-core:10.20.1")
        // https://mvnrepository.com/artifact/org.flywaydb/flyway-database-postgresql
        dependency("org.flywaydb:flyway-database-postgresql:10.20.1")
        // https://mvnrepository.com/artifact/com.vladmihalcea/hibernate-types-60
        dependency("com.vladmihalcea:hibernate-types-60:2.21.1")

        // mapper
        // https://mvnrepository.com/artifact/org.mapstruct/mapstruct
        dependency("org.mapstruct:mapstruct:1.6.2")
        // https://mvnrepository.com/artifact/org.mapstruct/mapstruct-processor
        dependency("org.mapstruct:mapstruct-processor:1.6.2")

        // docs
        // https://mvnrepository.com/artifact/io.swagger.core.v3/swagger-annotations
        dependency("io.swagger.core.v3:swagger-annotations:2.2.25")
        // https://mvnrepository.com/artifact/io.swagger.core.v3/swagger-models
        dependency("io.swagger.core.v3:swagger-models:2.2.25")
        // https://mvnrepository.com/artifact/com.github.scribejava/scribejava-core
        dependency("com.github.scribejava:scribejava-core:8.3.3")
        // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui
        dependency("org.springdoc:springdoc-openapi-ui:1.8.0")

        // sentry
        // https://mvnrepository.com/artifact/io.sentry/sentry-spring-boot-starter-jakarta
        dependency("io.sentry:sentry-spring-boot-starter-jakarta:7.16.0")
        // https://mvnrepository.com/artifact/io.sentry/sentry-logback
        dependency("io.sentry:sentry-logback:7.16.0")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

// using "artifact bomZip" makes the project a pom packaging
tasks.register<Zip>("bomZip") {
    group = "build"
    description = "create zip from bom"
    archiveFileName.set("bom-${project.version}.zip")
    from(layout.buildDirectory.dir("publications/maven"))
    include("*.xml")
    dependsOn("generatePomFileForMavenPublication")
}

// disable gradle-metadata
tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

nexusPublishing {
    repositories {
        sonatype {
            // needed because default was updated to another server that this project can't use atm
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(project.findProperty("ossrhUsername") as String?)
            password.set(project.findProperty("ossrhPassword") as String?)
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(tasks["bomZip"])

            pom {
                name.set("Spring Boot BOM")
                description.set("BOM for Spring Boot based projects")
                url.set("https://mbo.dev")
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                scm {
                    url.set("https://github.com/mbogner/spring-boot-bom")
                    connection.set("git@github.com:mbogner/spring-boot-bom.git")
                    developerConnection.set("git@github.com:mbogner/spring-boot-bom.git")
                }
                developers {
                    developer {
                        id.set("mbo")
                        name.set("Manuel Bogner")
                        email.set("outrage_breath.0t@icloud.com")
                        organization.set("mbo.dev")
                        organizationUrl.set("https://mbo.dev")
                        timezone.set("Europe/Vienna")
                        roles.set(listOf("developer", "architect"))
                    }
                }
                organization {
                    name.set("mbo.dev")
                    url.set("https://mbo.dev")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}

tasks.wrapper {
    // https://gradle.org/releases/
    gradleVersion = "8.10.2"
    distributionType = Wrapper.DistributionType.BIN
}