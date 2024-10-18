plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("maven-publish")
}

group = "de.turboman"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val minestom = "net.minestom:minestom-snapshots:d0754f2a15"

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(minestom)

    compileOnly(minestom)

    implementation("dev.dewy:nbt:1.5.1")
}

tasks.test {
    useJUnitPlatform()
}

