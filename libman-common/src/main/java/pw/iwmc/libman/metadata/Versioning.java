package pw.iwmc.libman.metadata;

import pw.iwmc.libman.metadata.snapshot.SnapshotVersions;

import javax.xml.bind.annotation.XmlElement;

public record Versioning(String lastUpdated, SnapshotVersions snapshotVersions) {

    @XmlElement
    public String lastUpdated() {
        return lastUpdated;
    }

    @XmlElement
    public SnapshotVersions snapshotVersions() {
        return snapshotVersions;
    }
}
