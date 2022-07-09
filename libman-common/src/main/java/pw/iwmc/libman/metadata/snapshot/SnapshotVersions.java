package pw.iwmc.libman.metadata.snapshot;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public record SnapshotVersions(List<SnapshotVersion> versions) {

    @XmlElement
    public List<SnapshotVersion> snapshotVersion() {
        return versions;
    }

}
