# Libman
Simple lightweight dependency manager.

![Percentage of issues still open](https://img.shields.io/github/issues/iwmc-git/Libman?style=for-the-badge)
[![GitHub forks](https://img.shields.io/github/forks/iwmc-git/Libman?style=for-the-badge)](https://github.com/iwmc-git/Libman/network)
[![GitHub stars](https://img.shields.io/github/stars/iwmc-git/Libman?style=for-the-badge)](https://github.com/iwmc-git/Libman/stargazers)
[![GitHub license](https://img.shields.io/github/license/iwmc-git/Libman?style=for-the-badge)](https://github.com/iwmc-git/Libman/blob/master/LICENSE) 

## Usage
First, add dependency in your project.

### Maven
```dtd
<repositories>
    <repository>
        <id>icewynd-repository-releases</id>
        <name>Icewynd`s repository</name>
        <url>https://maven.iwmc.pw/releases</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>pw.iwmc.libman</groupId>
        <artifactId>libman-api</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>pw.iwmc.libman</groupId>
        <artifactId>libman-comon</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Gradle Kotlin
```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.iwmc.pw/releases")
    }
}

dependencies {
    implementation("pw.iwmc.libman:libman-api:1.0.0")
    implementation("pw.iwmc.libman:libman-common:1.0.0")
}
```

### Initialization
```java
public class LibmanTest {
    public static void main(String[] args) {
        var rootPath = Path.of("Your root project path");

        if (Files.notExists(rootPath)) {
            Files.createDirectory(rootPath);
        }

        // test dependencies
        var nonSnapshotDependency = Dependency.of("org.mariadb.jdbc", "mariadb-java-client", "3.0.6");
        var snapshotDependency = Dependency.of("net.kyori", "adventure-api", "4.11.0-SNAPSHOT");
        
        var mavenRepo = Repository.of("maven-central", "https://repo1.maven.org/maven2/");
        var sonatypeSnapshots = Repository.of("sonatype-snapshots", "https://oss.sonatype.org/content/repositories/snapshots/");

        // remap property
        var remap = Remap.of("org.mariadb.jdbc", "pw.iwmc.libman.builtin-libs.mariadb");

        var libman = new Libman(rootPath, List.of(mavenRepo, sonatypeSnapshots), true, true);

        libman.downloader().downloadDependency(nonSnapshotDependency);
        libman.remapper().remap(nonSnapshotDependency, List.of(remap));

        libman.downloader().downloadDependency(snapshotDependency);
        libman.remapper().remap(snapshotDependency, null);
    }
}
```