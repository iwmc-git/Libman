package pw.iwmc.libman;

import org.jetbrains.annotations.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pw.iwmc.libman.api.LibmanAPI;
import pw.iwmc.libman.api.downloader.Downloader;
import pw.iwmc.libman.api.objects.Dependency;
import pw.iwmc.libman.api.objects.Repository;
import pw.iwmc.libman.api.remapper.Remapper;
import pw.iwmc.libman.downloader.LibraryDownloader;
import pw.iwmc.libman.remapper.LibraryRemapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Libman implements LibmanAPI {
    private static Libman libman;

    private final Map<Dependency, Path> downloaded;
    private final boolean useRemapper;
    private Map<Dependency, Path> remapped;

    private final Downloader downloader;
    private Remapper remapper;

    private final Logger logger;

    private final boolean enableLogger;
    private final boolean checkFileHashes;

    private Path remappedDependsFolder;
    private final Path downloadedDependsFolder;

    private final List<Repository> repositories;

    public Libman(@NotNull Path rootPath, List<Repository> repositories, boolean enableLogger, boolean checkFileHashes, boolean useRemapper) {
        libman = this;

        this.enableLogger = enableLogger;
        this.useRemapper = useRemapper;

        this.logger = enableLogger ? LoggerFactory.getLogger("libman::main") : null;

        this.log("Loading Libman...");
        this.log("Remapping function is " + (useRemapper ? "activated!" : "disabled!"));

        try {
            this.downloadedDependsFolder = Paths.get(rootPath.toString(), "downloaded-libs").toAbsolutePath();

            if (useRemapper) {
                this.remappedDependsFolder = Paths.get(rootPath.toString(), "remapped-libs").toAbsolutePath();

                if (Files.notExists(remappedDependsFolder)) {
                    log("Creating the directory for downloads...");
                    Files.createDirectory(remappedDependsFolder);
                }
            }

            if (Files.notExists(downloadedDependsFolder)) {
                log("Creating the directory for remappings...");
                Files.createDirectory(downloadedDependsFolder);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        log("Initializing maps...");
        this.downloaded = new HashMap<>();

        this.checkFileHashes = checkFileHashes;
        this.repositories = repositories;

        this.downloader = new LibraryDownloader();

        if (useRemapper) {
            this.remapped = new HashMap<>();
            this.remapper = new LibraryRemapper();
        }
    }

    @Override
    public Map<Dependency, Path> downloaded() {
        return downloaded;
    }

    @Override
    public Map<Dependency, Path> remapped() {
        return remapped;
    }

    @Override
    public Path downloadedDependsFolder() {
        return downloadedDependsFolder;
    }

    @Override
    public Path remappedDependsFolder() {
        return remappedDependsFolder;
    }

    @Override
    public Downloader downloader() {
        return downloader;
    }

    @Override
    public Remapper remapper() {
        return remapper;
    }

    public List<Repository> repositories() {
        return repositories;
    }

    public static Libman libman() {
        return libman;
    }

    public void log(String message) {
        if (enableLogger) logger.info(message);
    }

    public boolean checkFileHashes() {
        return checkFileHashes;
    }

    public boolean useRemapper() {
        return useRemapper;
    }
}
