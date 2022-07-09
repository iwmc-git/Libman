package pw.iwmc.libman.metadata;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public record Metadata(String groupId, String artifactId, String version, Versioning versioning) {

    @XmlElement
    public String groupId() {
        return groupId;
    }

    @XmlElement
    public String artifactId() {
        return artifactId;
    }

    @XmlElement
    public String version() {
        return version;
    }

    @XmlElement
    public Versioning versioning() {
        return versioning;
    }
}
