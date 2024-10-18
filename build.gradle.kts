plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("maven-publish")
}

group = "de.turboman.libs"
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

tasks {
    shadowJar {
        archiveClassifier.set("")
    }

    artifacts {
        add("archives", shadowJar.get())
    }
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.turboman.libs"
            artifactId = "s2e"
            version = "1.0"

            from(components["java"])
        }
    }
}