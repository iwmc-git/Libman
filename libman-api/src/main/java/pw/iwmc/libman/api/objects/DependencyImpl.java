package pw.iwmc.libman.api.objects;

import java.util.Objects;

/**
 * Private implementation of ${@link Dependency}.
 */
final class DependencyImpl implements Dependency {
    private final String groupId;
    private final String artifactId;
    private final String version;

    private String name;

    public DependencyImpl(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    @Override
    public String groupId() {
        return groupId;
    }

    @Override
    public String artifactId() {
        return artifactId;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public boolean snapshot() {
        return version.endsWith("-SNAPSHOT");
    }

    @Override
    public String artifactName() {
        return Objects.requireNonNullElseGet(name, () -> artifactId + "-" + version);
    }

    @Override
    public void newArtifactName(String name) {
        this.name = name;
    }
}
