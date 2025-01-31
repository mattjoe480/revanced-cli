plugins {
    kotlin("jvm") version "1.8.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.github.inotia00"

repositories {
    google()
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://plugins.gradle.org/m2/") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.22")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.22")

    implementation("io.github.inotia00:revanced-patcher:11.0.6-SNAPSHOT")
    implementation("info.picocli:picocli:4.7.4")
    implementation("com.github.revanced:jadb:2531a28109") // updated fork
    implementation("com.android.tools.build:apksig:8.2.0-alpha13")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
}

kotlin {
    jvmToolchain(11)
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("PASSED", "SKIPPED", "FAILED")
        }
    }
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        manifest {
            attributes("Main-Class" to "app.revanced.cli.main.MainKt")
        }
        minimize {
            exclude(dependency("org.jetbrains.kotlin:.*"))
            exclude(dependency("org.bouncycastle:.*"))
            exclude(dependency("app.revanced:.*"))
        }
    }
    // Dummy task to fix the Gradle semantic-release plugin.
    // Remove this if you forked it to support building only.
    // Tracking issue: https://github.com/KengoTODA/gradle-semantic-release-plugin/issues/435
    register<DefaultTask>("publish") {
        group = "publish"
        description = "Dummy task"
        dependsOn(build)
    }
}
