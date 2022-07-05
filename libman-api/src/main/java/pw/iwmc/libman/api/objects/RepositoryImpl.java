package pw.iwmc.libman.api.objects;

/**
 * Private implementation of ${@link Repository}.
 *
 * @param name repository name.
 * @param url repository url.
 */
record RepositoryImpl(String name, String url) implements Repository { }
