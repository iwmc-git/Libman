package pw.iwmc.libman.metadata;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Metadata {
    private String groupId;
    private String artifactId;
    private String version;
    private Versioning versioning;

    public Metadata() {
    }

    public Metadata(String groupId, String artifactId, String version, Versioning versioning) {
        super();
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.versioning = versioning;
    }

    @XmlElement
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @XmlElement
    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    @XmlElement
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlElement
    public Versioning getVersioning() {
        return versioning;
    }

    public void setVersioning(Versioning versioning) {
        this.versioning = versioning;
    }
}
