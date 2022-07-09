dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")

    api("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

    api("org.slf4j:slf4j-api:1.7.36")
    api("org.slf4j:slf4j-simple:1.7.36")

    api(project(":libman-api"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            artifactId = "libman-common"

            from(components["java"])
        }
    }
}