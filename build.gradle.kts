import java.time.Year

plugins {
    `java-library` // we don't want to create a jar but found no other working solution so far
    `maven-publish`
    id("io.spring.dependency-management") version "1.1.7" // https://plugins.gradle.org/plugin/io.spring.dependency-management
    id("net.researchgate.release") version "3.1.0" // https://plugins.gradle.org/plugin/net.researchgate.release
    id("org.jreleaser") version "1.19.0" // https://plugins.gradle.org/plugin/org.jreleaser
}

group = "dev.mbo"
val year = Year.now().value

dependencyManagement {
    imports {
        val springCloudVersion = "2025.0.0"
        mavenBom("org.springframework.cloud:spring-cloud-starter-parent:$springCloudVersion")
        // https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-parent
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        // https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-dependencies

        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.5")
        // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-dependencies
        mavenBom("org.jetbrains.kotlin:kotlin-bom:2.2.10")
        // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-bom
        mavenBom("org.testcontainers:testcontainers-bom:1.21.3")
        // https://mvnrepository.com/artifact/org.testcontainers/testcontainers-bom
        mavenBom("software.amazon.awssdk:bom:2.32.30")
        // https://mvnrepository.com/artifact/software.amazon.awssdk/bom
        mavenBom("io.ktor:ktor-bom:3.2.3")
        // https://mvnrepository.com/artifact/io.ktor/ktor-bom
    }
    dependencies {
        dependency("org.apache.commons:commons-lang3:3.18.0")
        // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
        dependency("commons-io:commons-io:2.20.0")
        // https://mvnrepository.com/artifact/commons-io/commons-io
        dependency("commons-codec:commons-codec:1.19.0")
        // https://mvnrepository.com/artifact/commons-codec/commons-codec
        dependency("org.apache.commons:commons-pool2:2.12.1")
        // https://mvnrepository.com/artifact/org.apache.commons/commons-pool2
        dependency("org.freemarker:freemarker:2.3.34")
        // https://mvnrepository.com/artifact/org.freemarker/freemarker
        dependency("org.keycloak:keycloak-admin-client:26.0.6")
        // https://mvnrepository.com/artifact/org.keycloak/keycloak-admin-client
        dependency("com.redis:testcontainers-redis:2.2.4")
        // https://mvnrepository.com/artifact/com.redis/testcontainers-redis
        dependency("org.mockito.kotlin:mockito-kotlin:6.0.0")
        // https://mvnrepository.com/artifact/org.mockito.kotlin/mockito-kotlin
        dependency("com.aallam.openai:openai-client:4.0.1")
        // https://mvnrepository.com/artifact/com.aallam.openai/openai-client
        dependency("org.jsoup:jsoup:1.21.2")
        // https://mvnrepository.com/artifact/org.jsoup/jsoup
        dependency("org.wiremock:wiremock:3.13.1")
        // https://mvnrepository.com/artifact/org.wiremock/wiremock
        dependency("io.nats:jnats:2.21.5")
        // https://mvnrepository.com/artifact/io.nats/jnats
        dependency("org.passay:passay:1.6.6") // password check
        // https://mvnrepository.com/artifact/org.passay/passay
        dependency("org.apache.commons:commons-compress:1.28.0")
        // https://mvnrepository.com/artifact/org.apache.commons/commons-compress

        val zxingVersion = "3.5.3"
        dependency("com.google.zxing:core:$zxingVersion")
        // QR-Code - https://mvnrepository.com/artifact/com.google.zxing/core
        dependency("com.google.zxing:javase:$zxingVersion")
        // https://mvnrepository.com/artifact/com.google.zxing/javase

        dependency("org.apache.httpcomponents.core5:httpcore5:5.3.4") // http client
        // https://mvnrepository.com/artifact/org.apache.httpcomponents.core5/httpcore5
        dependency("org.apache.httpcomponents.client5:httpclient5:5.5")
        // https://mvnrepository.com/artifact/org.apache.httpcomponents.client5/httpclient5

        dependency("org.postgresql:postgresql:42.7.7") // database
        // https://mvnrepository.com/artifact/org.postgresql/postgresql
        val flywayVersion = "11.11.2"
        // https://mvnrepository.com/artifact/org.flywaydb/flyway-core
        dependency("org.flywaydb:flyway-core:$flywayVersion")
        dependency("org.flywaydb:flyway-database-postgresql:$flywayVersion")

        val swaggerVersion = "2.2.36"
        // https://mvnrepository.com/artifact/io.swagger.core.v3/swagger-annotations
        dependency("io.swagger.core.v3:swagger-annotations:$swaggerVersion") // docs
        dependency("io.swagger.core.v3:swagger-models:$swaggerVersion")
        dependency("com.github.scribejava:scribejava-core:8.3.3")
        // https://mvnrepository.com/artifact/com.github.scribejava/scribejava-core
        dependency("org.springdoc:springdoc-openapi-ui:1.8.0")
        // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui

        val archUnitVersion = "1.4.1"
        // https://mvnrepository.com/artifact/com.tngtech.archunit/archunit
        dependency("com.tngtech.archunit:archunit:$archUnitVersion")
        dependency("com.tngtech.archunit:archunit-junit5:$archUnitVersion")

        // gradle plugin: https://mvnrepository.com/artifact/io.sentry.jvm.gradle/io.sentry.jvm.gradle.gradle.plugin
        // see https://docs.sentry.io/platforms/java/guides/spring-boot/ for compatibility
        dependency("io.sentry:sentry-spring-boot-starter-jakarta:8.20.0")
        // https://mvnrepository.com/artifact/io.sentry/sentry-spring-boot-starter-jakarta
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

tasks {
    withType<GenerateModuleMetadata>().configureEach {
        enabled = false
    }

    named("jar") {
        enabled = false
    }

    named("jreleaserFullRelease") {
        dependsOn("publish")
    }

    named("afterReleaseBuild") {
        dependsOn("publishToMavenLocal", "jreleaserFullRelease")
    }

    named<Wrapper>("wrapper") {
        gradleVersion = "9.0.0"
        // https://gradle.org/releases/
        distributionType = Wrapper.DistributionType.BIN
    }
}

jreleaser {
    project {
        name.set("spring-boot-bom")
        description.set("Spring Boot BOM for mbo.dev projects")
        longDescription.set("A curated BOM (Bill of Materials) for Spring Boot projects aligned with Kotlin and cloud-native practices.")
        license.set("Apache-2.0")
        copyright.set("\u00a9 $year mbo.dev")
        authors.set(listOf("Manuel Bogner"))
        tags.set(listOf("spring", "boot", "bom", "dependencies", "kotlin"))
        links {
            homepage.set("https://mbo.dev")
            documentation.set("https://github.com/mbogner/spring-boot-bom")
        }
    }

    signing {
        active.set(org.jreleaser.model.Active.ALWAYS)
        armored.set(true)
    }

    deploy {
        maven {
            mavenCentral {
                register("sonatype") {
                    active.set(org.jreleaser.model.Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    snapshotSupported.set(true)
                    stagingRepository("${layout.buildDirectory.get()}/staging-deploy")
                }
            }
        }
    }

    release {
        github {
            tagName.set("{{projectVersion}}")
            releaseName.set("Spring Boot BOM {{projectVersion}}")
        }
    }

    files {
        artifact {
            path.set(file("${layout.buildDirectory.get()}/publications/maven/pom-default.xml"))
            extraProperties.put("deployer", "maven:mavenCentral")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            // mark it as a pom-only publication
            pom {
                name.set("Spring Boot BOM")
                description.set("BOM for Spring Boot based projects")
                url.set("https://mbo.dev")
                packaging = "pom"

                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                scm {
                    url.set("https://github.com/mbogner/spring-boot-bom")
                    connection.set("scm:git:git://github.com/mbogner/spring-boot-bom.git")
                    developerConnection.set("scm:git:ssh://git@github.com/mbogner/spring-boot-bom.git")
                }
                developers {
                    developer {
                        id.set("mbo")
                        name.set("Manuel Bogner")
                        email.set("outrage_breath.0t@icloud.com")
                        organization.set("mbo.dev")
                        organizationUrl.set("https://mbo.dev")
                        roles.set(listOf("developer", "architect"))
                        timezone.set("Europe/Vienna")
                    }
                }
                organization {
                    name.set("mbo.dev")
                    url.set("https://mbo.dev")
                }
            }

            // prevent Gradle from attaching the default JAR
            suppressAllPomMetadataWarnings()
        }
    }
    repositories {
        maven {
            name = "staging"
            url = uri("${layout.buildDirectory.get()}/staging-deploy")
        }
    }
}
