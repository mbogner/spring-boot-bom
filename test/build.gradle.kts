plugins {
    `java-library`
    // https://plugins.gradle.org/plugin/io.spring.dependency-management
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.mbo"

dependencyManagement {
    imports {
        // https://s01.oss.sonatype.org/content/groups/public/dev/mbo/spring-boot-bom/
        // https://s01.oss.sonatype.org/service/local/repositories/releases/content/dev/mbo/spring-boot-bom/
        mavenBom("dev.mbo:spring-boot-bom:2024.12.2")
    }
    resolutionStrategy {
        cacheChangingModulesFor(0, "seconds")
    }
}

dependencies {
    implementation("org.apache.commons:commons-lang3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://s01.oss.sonatype.org/content/groups/public")
    }
    mavenCentral()
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_21.toString()
        targetCompatibility = JavaVersion.VERSION_21.toString()
        options.isIncremental = true
    }

    withType<Test> {
        useJUnitPlatform()
    }

    wrapper {
        // https://gradle.org/releases/
        gradleVersion = "8.12"
        distributionType = Wrapper.DistributionType.BIN
    }
}