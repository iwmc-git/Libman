package pw.iwmc.libman.api.objects;

import org.jetbrains.annotations.NotNull;

/**
 * Main interface of ${@link Dependency} object.
 */
public interface Dependency {

    /**
     * Returns dependency group id.
     * @return the group id.
     */
    String groupId();

    /**
     * Returns dependency artifact id.
     * @return the artifact id.
     */
    String artifactId();

    /**
     * Returns dependency version.
     * @return the version.
     */
    String version();

    /**
     * Returns artifact name.
     * <p>As artifact_id-version.jar</p>
     *
     * @return the artifact name.
     */
    String artifactName();

    /**
     * Returns state, if this
     * dependency has snapshot version.
     *
     * @return true if dependency version ends with '-SNAPSHOT'.
     */
    boolean snapshot();

    /**
     * Updates artifact name.
     * @param name artifact name.
     */
    void newArtifactName(String name);

    /**
     * Creates new ${@link Dependency} object.
     *
     * @param groupId dependency group id.
     * @param artifactId dependency artifact id.
     * @param version dependency version.
     *
     * @return the dependency object.
     */
    static @NotNull Dependency of(String groupId, String artifactId, String version) {
        return new DependencyImpl(groupId, artifactId, version);
    }

    /**
     * Creates new ${@link Dependency} object from gradle format.
     *
     * @param format gradle dependency format.
     *
     * @return the dependency object.
     */
    static @NotNull Dependency of(@NotNull String format) {
        var split = format.split(":");
        return new DependencyImpl(split[0], split[1], split[2]);
    }
}
