package pw.iwmc.libman.api.downloader;

import org.jetbrains.annotations.NotNull;
import pw.iwmc.libman.api.objects.Dependency;

/**
 * Main of a ${@link Downloader}
 */
public interface Downloader {

    /**
     * Downloads dependency.
     * @param dependency the dependency.
     */
    void downloadDependency(@NotNull Dependency dependency);
}
