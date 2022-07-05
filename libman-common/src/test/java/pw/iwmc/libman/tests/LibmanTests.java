package pw.iwmc.libman.tests;

import org.junit.jupiter.api.Test;

import pw.iwmc.libman.Libman;
import pw.iwmc.libman.api.objects.Dependency;
import pw.iwmc.libman.api.objects.Remap;
import pw.iwmc.libman.api.objects.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LibmanTests {

    @Test
    public void test_dependency_download_and_remap_and_check_filehashes() throws IOException {
        var rootPath = Path.of("libraries");

        if (Files.notExists(rootPath)) {
            Files.createDirectory(rootPath);
        }

        var nonSnapshotDependency = Dependency.of("org.mariadb.jdbc", "mariadb-java-client", "3.0.6");
        var snapshotDependency = Dependency.of("net.kyori", "adventure-api", "4.11.0-SNAPSHOT");

        var mavenRepo = Repository.of("maven-central", "https://repo1.maven.org/maven2/");
        var sonatypeSnapshots = Repository.of("sonatype-snapshots", "https://oss.sonatype.org/content/repositories/snapshots/");

        var remap = Remap.of("org.mariadb.jdbc", "pw.iwmc.libman.builtin-libs.mariadb");

        var libman = new Libman(rootPath, List.of(mavenRepo, sonatypeSnapshots), true, true);

        libman.downloader().downloadDependency(nonSnapshotDependency);
        libman.remapper().remap(nonSnapshotDependency, List.of(remap));

        libman.downloader().downloadDependency(snapshotDependency);
        libman.remapper().remap(snapshotDependency, null);
    }
}
