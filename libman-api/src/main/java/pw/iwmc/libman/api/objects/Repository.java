package pw.iwmc.libman.api.objects;

import org.jetbrains.annotations.NotNull;

/**
 * Main interface of ${@link Repository} object.
 */
public interface Repository {

    /**
     * Returns the name of the repositoryю
     * @return repository name.
     */
    String name();

    /**
     * Returns a url to the repository.
     * @return repository url.
     */
    String url();

    /**
     * Creates new ${@link Repository} object.
     *
     * @param name repository name.
     * @param url repository url.
     *
     * @return the repository object.
     */
    static @NotNull Repository of(String name, String url) {
        return new RepositoryImpl(name, url);
    }
}
