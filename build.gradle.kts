plugins {
    id("java-library")
    id("maven-publish")
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "pw.iwmc.libman"
    version = "1.0.0-SNAPSHOT"
    description = "Simple lightweight dependency manager."

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        withSourcesJar()
    }

    repositories {
        mavenCentral()
    }
}