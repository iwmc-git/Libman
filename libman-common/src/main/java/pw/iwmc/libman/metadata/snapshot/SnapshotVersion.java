package pw.iwmc.libman.metadata.snapshot;

import javax.xml.bind.annotation.XmlElement;

public record SnapshotVersion(String classifier, String extension, String value, String updated) {

    @XmlElement
    public String classifier() {
        return classifier;
    }

    @XmlElement
    public String extension() {
        return extension;
    }

    @XmlElement
    public String value() {
        return value;
    }

    @XmlElement
    public String updated() {
        return updated;
    }
}
