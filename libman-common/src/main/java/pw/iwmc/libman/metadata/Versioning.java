package pw.iwmc.libman.metadata;

import pw.iwmc.libman.metadata.snapshot.SnapshotVersions;

import javax.xml.bind.annotation.XmlElement;

public class Versioning {
    private String lastUpdated;
    private SnapshotVersions snapshotVersions;

    public Versioning() {
    }

    public Versioning(String lastUpdated, SnapshotVersions snapshotVersions) {
        super();
        this.lastUpdated = lastUpdated;
        this.snapshotVersions = snapshotVersions;
    }

    @XmlElement
    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @XmlElement
    public SnapshotVersions getSnapshotVersions() {
        return snapshotVersions;
    }

    public void setSnapshotVersions(SnapshotVersions snapshotVersions) {
        this.snapshotVersions = snapshotVersions;
    }
}
