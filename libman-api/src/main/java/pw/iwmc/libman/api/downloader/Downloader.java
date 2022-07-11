package pw.iwmc.libman.api.downloader;

import org.jetbrains.annotations.NotNull;
import pw.iwmc.libman.api.objects.Dependency;
import pw.iwmc.libman.api.objects.Repository;

/**
 * Main of a ${@link Downloader}
 */
public interface Downloader {

    /**
     * Downloads dependency.
     * @param dependency the dependency.
     */
    void downloadDependency(@NotNull Dependency dependency);

    /**
     * Downloads dependency.
     *
     * @param dependency the dependency.
     * @param repository the repository.
     *
     * @return true if dependency successfuly downloaded.
     *
     * @throws Exception if an error occurs during execution.
     */
    boolean downloadDependency(Dependency dependency, @NotNull Repository repository) throws Exception;

    /**
     * Downloads snapshot dependency.
     *
     * @param dependency the dependency.
     * @param repository the repository.
     *
     * @return true if dependency successfuly downloaded.
     *
     * @throws Exception if an error occurs during execution.
     */
    boolean downloadSnapshotDependency(Dependency dependency, @NotNull Repository repository) throws Exception;
}
