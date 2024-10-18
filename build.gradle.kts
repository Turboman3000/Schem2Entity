plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("maven-publish")
}

group = "de.turboman.s2e"
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

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Turboman3000/Schem2Entity")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}