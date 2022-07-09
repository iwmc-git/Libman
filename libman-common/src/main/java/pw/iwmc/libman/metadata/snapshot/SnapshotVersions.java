package pw.iwmc.libman.metadata.snapshot;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class SnapshotVersions {
    private List<SnapshotVersion> versions;

    public SnapshotVersions() {
    }

    public SnapshotVersions(List<SnapshotVersion> versions) {
        super();
        this.versions = versions;
    }

    @XmlElement
    public List<SnapshotVersion> getSnapshotVersion() {
        return versions;
    }

    public void setSnapshotVersion(List<SnapshotVersion> snapshotVersions) {
        this.versions = snapshotVersions;
    }
}
