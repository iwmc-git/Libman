plugins {
    id("java-library")
    id("maven-publish")
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "pw.iwmc.libman"
    version = "1.0.7"
    description = "Simple lightweight dependency manager."

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        withSourcesJar()
    }

    repositories {
        mavenCentral()
    }

    publishing {
        repositories {
            maven {
                name = "icewynd-repository"

                val releases = "https://maven.iwmc.pw/releases/"
                val snapshots = "https://maven.iwmc.pw/snapshots/"

                val finalUrl = if (rootProject.version.toString().endsWith("SNAPSHOT")) snapshots else releases

                url = uri(finalUrl)

                credentials {
                    username = System.getenv("REPO_USERNAME")
                    password = System.getenv("REPO_TOKEN")
                }
            }
        }
    }
}