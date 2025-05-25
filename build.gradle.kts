import java.time.Year

plugins {
    `java-library` // we don't want to create a jar but found no other working solution so far
    signing // required for maven central
    `maven-publish`
    id("io.spring.dependency-management") version "1.1.7" // https://plugins.gradle.org/plugin/io.spring.dependency-management
    id("net.researchgate.release") version "3.1.0" // https://plugins.gradle.org/plugin/net.researchgate.release
    id("org.jreleaser") version "1.18.0" // https://jreleaser.org/guide/latest/examples/maven/maven-central.html#_gradle
}

group = "dev.mbo"
val year = Year.now().value

dependencyManagement {
    imports {
        val springCloudVersion = "2024.0.1"
        mavenBom("org.springframework.cloud:spring-cloud-starter-parent:$springCloudVersion")
        // https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-parent
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        // https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-dependencies

        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.0")
        // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-dependencies
        mavenBom("org.jetbrains.kotlin:kotlin-bom:2.1.21")
        // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-bom
        mavenBom("org.testcontainers:testcontainers-bom:1.21.0")
        // https://mvnrepository.com/artifact/org.testcontainers/testcontainers-bom
        mavenBom("software.amazon.awssdk:bom:2.31.50")
        // https://mvnrepository.com/artifact/software.amazon.awssdk/bom
        mavenBom("io.ktor:ktor-bom:3.1.3")
        // https://mvnrepository.com/artifact/io.ktor/ktor-bom
    }
    dependencies {
        dependency("org.apache.commons:commons-lang3:3.17.0")
        // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
        dependency("commons-io:commons-io:2.19.0")
        // https://mvnrepository.com/artifact/commons-io/commons-io
        dependency("org.apache.commons:commons-pool2:2.12.1")
        // https://mvnrepository.com/artifact/org.apache.commons/commons-pool2
        dependency("org.freemarker:freemarker:2.3.34")
        // https://mvnrepository.com/artifact/org.freemarker/freemarker
        dependency("org.keycloak:keycloak-admin-client:26.0.5")
        // https://mvnrepository.com/artifact/org.keycloak/keycloak-admin-client
        dependency("com.redis:testcontainers-redis:2.2.4")
        // https://mvnrepository.com/artifact/com.redis/testcontainers-redis
        dependency("org.mockito.kotlin:mockito-kotlin:5.4.0")
        // https://mvnrepository.com/artifact/org.mockito.kotlin/mockito-kotlin
        dependency("com.aallam.openai:openai-client:4.0.1")
        // https://mvnrepository.com/artifact/com.aallam.openai/openai-client
        dependency("org.jsoup:jsoup:1.20.1")
        // https://mvnrepository.com/artifact/org.jsoup/jsoup
        dependency("org.wiremock:wiremock:3.13.0")
        // https://mvnrepository.com/artifact/org.wiremock/wiremock
        dependency("io.nats:jnats:2.21.1")
        // https://mvnrepository.com/artifact/io.nats/jnats
        dependency("org.passay:passay:1.6.6") // password check
        dependency("org.apache.commons:commons-compress:1.27.1")
        // https://mvnrepository.com/artifact/org.apache.commons/commons-compress
        dependency("com.eatthepath:java-otp:0.4.0")
        // OTP - https://mvnrepository.com/artifact/com.eatthepath/java-otp

        val zxingVersion = "3.5.3"
        dependency("com.google.zxing:core:$zxingVersion")
        // QR-Code - https://mvnrepository.com/artifact/com.google.zxing/core
        dependency("com.google.zxing:javase:$zxingVersion")
        // https://mvnrepository.com/artifact/com.google.zxing/javase

        dependency("org.apache.httpcomponents.core5:httpcore5:5.3.4") // http client
        dependency("org.apache.httpcomponents.client5:httpclient5:5.5")

        val jjwtVersion = "0.12.6"
        dependency("io.jsonwebtoken:jjwt-api:$jjwtVersion") // jwt
        dependency("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
        dependency("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

        dependency("org.postgresql:postgresql:42.7.5") // database
        val flywayVersion = "11.8.2"
        dependency("org.flywaydb:flyway-core:$flywayVersion")
        dependency("org.flywaydb:flyway-database-postgresql:$flywayVersion")

        dependency("com.vladmihalcea:hibernate-types-60:2.21.1") // hibernate types

        val mapstructVersion = "1.6.3"
        dependency("org.mapstruct:mapstruct:$mapstructVersion") // mapper
        dependency("org.mapstruct:mapstruct-processor:$mapstructVersion")

        val swaggerVersion = "2.2.30"
        dependency("io.swagger.core.v3:swagger-annotations:$swaggerVersion") // docs
        dependency("io.swagger.core.v3:swagger-models:$swaggerVersion")
        dependency("com.github.scribejava:scribejava-core:8.3.3")
        dependency("org.springdoc:springdoc-openapi-ui:1.8.0")

        val archUnitVersion = "1.4.1"
        dependency("com.tngtech.archunit:archunit:$archUnitVersion")
        dependency("com.tngtech.archunit:archunit-junit5:$archUnitVersion")

        // downgrade for io.sentry.jvm.gradle 5.4.0 -> https://mvnrepository.com/artifact/io.sentry.jvm.gradle/io.sentry.jvm.gradle.gradle.plugin
        // try upgrading the plugin and check compatibility of the sentry-spring-boot-starter-jakarta before changing here
        dependency("io.sentry:sentry-spring-boot-starter-jakarta:8.12.0")
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
        dependsOn("signMavenPublication", "publishToMavenLocal", "jreleaserFullRelease")
    }

    named<Wrapper>("wrapper") {
        gradleVersion = "8.14.1"
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

signing {
    sign(publishing.publications["maven"])
}
