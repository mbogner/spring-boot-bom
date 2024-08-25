plugins {
    `java-library` // we don't want to create a jar but found no other working solution so far
    signing // required for maven central
    `maven-publish`
    // https://plugins.gradle.org/plugin/io.spring.dependency-management
    id("io.spring.dependency-management") version "1.1.6"
}

group = "dev.mbo"

dependencyManagement {
    imports {
        // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-dependencies
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.3")
        // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-bom
        mavenBom("org.jetbrains.kotlin:kotlin-bom:2.0.20")
        // https://mvnrepository.com/artifact/org.testcontainers/testcontainers-bom
        mavenBom("org.testcontainers:testcontainers-bom:1.20.1")
    }
    dependencies {
        // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
        dependency("org.apache.commons:commons-lang3:3.16.0")

        // https://mvnrepository.com/artifact/org.mapstruct/mapstruct
        dependency("org.mapstruct:mapstruct:1.6.0")
        // https://mvnrepository.com/artifact/org.mapstruct/mapstruct-processor
        dependency("org.mapstruct:mapstruct-processor:1.6.0")

        // https://mvnrepository.com/artifact/io.swagger.core.v3/swagger-annotations
        dependency("io.swagger.core.v3:swagger-annotations:2.2.22")
        // https://mvnrepository.com/artifact/com.github.scribejava/scribejava-core
        dependency("com.github.scribejava:scribejava-core:8.3.3")
        // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui
        dependency("org.springdoc:springdoc-openapi-ui:1.8.0")

        // https://mvnrepository.com/artifact/org.freemarker/freemarker
        dependency("org.freemarker:freemarker:2.3.33")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

// using "artifact bomZip" makes the project a pom packaging
tasks.register<Zip>("bomZip") {
    archiveFileName.set("bom-${project.version}.zip")
    from(layout.buildDirectory.dir("publications/maven"))
    include("*.xml")
    dependsOn("generatePomFileForMavenPublication")
}

// disable gradle-metadata
tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

publishing {
    repositories {
        maven {
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            // val releasesRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/releases")

            // https://s01.oss.sonatype.org/content/repositories/snapshots/dev/mbo/spring-boot-bom
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = project.findProperty("ossrhUsername") as String?
                password = project.findProperty("ossrhPassword") as String?
            }
        }
    }

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
    gradleVersion = "8.10"
    distributionType = Wrapper.DistributionType.BIN
}