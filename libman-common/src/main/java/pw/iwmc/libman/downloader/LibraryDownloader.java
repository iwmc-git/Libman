package pw.iwmc.libman.downloader;

import org.jetbrains.annotations.NotNull;

import pw.iwmc.libman.Libman;
import pw.iwmc.libman.LibmanConstants;
import pw.iwmc.libman.LibmanUtils;
import pw.iwmc.libman.api.downloader.Downloader;
import pw.iwmc.libman.api.objects.Dependency;
import pw.iwmc.libman.api.objects.Repository;
import pw.iwmc.libman.metadata.helper.SnapshotHelper;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class LibraryDownloader implements Downloader {
    private final Libman libman = Libman.libman();
    private final boolean checkFileHashes = libman.checkFileHashes();

    public LibraryDownloader() {
        libman.log("Initializing downloader...");

        if (libman.useRemapper()) {
            if (Files.notExists(LibmanUtils.libraryPath(LibmanConstants.ASM, libman.downloadedDependsFolder()))) {
                this.downloadDependency(LibmanConstants.ASM);
                this.downloadDependency(LibmanConstants.ASM_COMMONS);
                this.downloadDependency(LibmanConstants.RELOCATOR);
            }
        }
    }

    @Override
    public void downloadDependency(@NotNull Dependency dependency) {
        download(dependency, libman.repositories());
    }

    private void download(@NotNull Dependency dependency, List<Repository> repositories) {
        var libraryPath = LibmanUtils.libraryPath(dependency, libman.downloadedDependsFolder());
        var found = false;

        if (Files.exists(libraryPath)) {
            libman.log(String.format("Dependency `%s.jar` exists! Skipping...", dependency.artifactName()));
            libman.addDependencyToDownloaded(dependency, libraryPath);
            return;
        }

        for (var repository : repositories) {
            try {
                if (found) continue;

                found = dependency.snapshot()
                        ? downloadSnapshotDependency(dependency, repository)
                        : downloadDependency(dependency, repository);

                if (checkFileHashes) checkHashes(dependency, repository);
            } catch (Exception exception) {
                // ignoring............. :p
            }
        }

        if (!found) {
            throw new RuntimeException("Could not download artifact `" + dependency.artifactId() + "` from any of the repositories");
        }

        libman.downloaded().put(dependency, LibmanUtils.libraryPath(dependency, libman.downloadedDependsFolder()));
    }

    @Override
    public void downloadDependencyFromRepo(@NotNull Dependency dependency, Repository repository) {
        var repositories = libman.repositories();
        repositories.add(repository);

        download(dependency, repositories);
    }

    @Override
    public boolean downloadSnapshotDependency(Dependency dependency, @NotNull Repository repository) throws Exception {
        var metaDataUrl = LibmanUtils.metadataUrl(dependency, repository.url());
        var urlConnection = openConnection(metaDataUrl);
        var status = urlConnection.getResponseCode();

        if ((status >= 200 && status < 300) || status == 304) {
            var stream = openStream(metaDataUrl);
            var latest = SnapshotHelper.readLastVersion(stream);

            dependency.newArtifactName(dependency.artifactId() + "-" + latest);
            libman.log(String.format("Dependency %s is snapshot! Update version to %s", dependency.artifactId(), latest));

            var artifactUrl = LibmanUtils.artifactUrl(dependency, repository.url());

            if (!LibmanUtils.libraryFile(dependency, libman.downloadedDependsFolder()).exists()) {
                libman.log(String.format("Trying download `%s.jar` from `%s`...", dependency.artifactName(), repository.name()));
                downloadFile(artifactUrl, LibmanUtils.libraryFile(dependency, libman.downloadedDependsFolder()));
            }

            return Files.exists(LibmanUtils.libraryPath(dependency, libman.downloadedDependsFolder()));
        }

        return false;
    }

    @Override
    public boolean downloadDependency(Dependency dependency, @NotNull Repository repository) throws Exception {
        var artifactUrl = LibmanUtils.artifactUrl(dependency, repository.url());
        var urlConnection = openConnection(artifactUrl);
        var status = urlConnection.getResponseCode();

        if ((status >= 200 && status < 300) || status == 304) {
            if (!LibmanUtils.libraryFile(dependency, libman.downloadedDependsFolder()).exists()) {
                libman.log(String.format("Trying download `%s.jar` from `%s`...", dependency.artifactName(), repository.name()));
                downloadFile(artifactUrl, LibmanUtils.libraryFile(dependency, libman.downloadedDependsFolder()));
            }

            return Files.exists(LibmanUtils.libraryPath(dependency, libman.downloadedDependsFolder()));
        }

        return false;
    }

    private void checkHashes(Dependency dependency, @NotNull Repository repository) throws Exception {
        libman.log("Checking file hash from repository...");

        var hashUrl = LibmanUtils.sha1Url(dependency, repository.url());
        var hashStream = openStream(hashUrl);
        var remoteFileHash = LibmanUtils.stringFromStream(hashStream);
        var localFileHash = LibmanUtils.fileHash(LibmanUtils.libraryFile(dependency, libman.downloadedDependsFolder()));

        libman.log(String.format("Remote hash for `%s.jar` from %s - %s", dependency.artifactName(), repository.name(), remoteFileHash));
        libman.log(String.format("Local hash for `%s.jar` from %s - %s", dependency.artifactName(), repository.name(), localFileHash));

        var validHash = localFileHash.equalsIgnoreCase(remoteFileHash);

        if (validHash) {
            libman.log(String.format("Dependency file %s hashes matches with each other!", dependency.artifactName()));
        } else {
            throw new RuntimeException("Artifact hash mismatch for file: " + LibmanUtils.libraryFile(dependency, libman.downloadedDependsFolder()).getName());
        }
    }

    private void downloadFile(URL url, @NotNull File output) throws Exception {
        var stream = openStream(url);

        if (Files.notExists(output.toPath())) {
            Files.createDirectories(output.toPath());
        }

        Files.copy(stream, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private InputStream openStream(URL url) throws Exception {
        var connection = openConnection(url);
        return connection.getInputStream();
    }

    private @NotNull HttpURLConnection openConnection(@NotNull URL url) throws Exception {
        var connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.75 Safari/535.7");
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        var status = connection.getResponseCode();
        var redirect = status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER;

        if (redirect) {
            var newUrl = connection.getHeaderField("Location");
            return openConnection(new URL(newUrl));
        }

        return connection;
    }
}
