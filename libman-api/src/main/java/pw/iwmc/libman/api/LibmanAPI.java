package pw.iwmc.libman.api;

import pw.iwmc.libman.api.downloader.Downloader;
import pw.iwmc.libman.api.objects.Dependency;
import pw.iwmc.libman.api.remapper.Remapper;

import java.nio.file.Path;
import java.util.Map;

/**
 * Main API class.
 */
public interface LibmanAPI {

    /**
     * Returns a ${@link Downloader} utility,
     * which allows you to download dependencies.
     *
     * @return the instance.
     */
    Downloader downloader();

    /**
     * Returns a ${@link Remapper} utility,
     * which allows you to relocate specific dependencies to a new location.
     *
     * @return the instance.
     */
    Remapper remapper();

    /**
     * Returns a map of downloaded dependencies
     * @return the map.
     */
    Map<Dependency, Path> downloaded();

    /**
     * Returns a list of remapped dependencies
     * @return the list.
     */
    Map<Dependency, Path> remapped();

    /**
     * Returns directory with downloaded dependencies.
     * @return the directory.
     */
    Path downloadedDependsFolder();

    /**
     * Returns directory with remapped dependencies.
     * @return the directory.
     */
    Path remappedDependsFolder();

    /**
     * Returns ${@link LibmanAPI} class instance.
     * @return the instance.
     */
    static LibmanAPI libman() {
        try {
            var implClass = Class.forName("pw.iwmc.libman.Libman");
            var libmanMethod = implClass.getDeclaredMethod("libman");
            return (LibmanAPI) libmanMethod.invoke(null);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
