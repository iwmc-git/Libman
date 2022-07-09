package pw.iwmc.libman.metadata.helper;

import pw.iwmc.libman.metadata.Metadata;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class SnapshotHelper {
    private final static JAXBContext JAXB_CONTEXT;
    private final static Unmarshaller JAXB_UNMARSHALLER;

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(Metadata.class);
            JAXB_UNMARSHALLER = JAXB_CONTEXT.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readLastVersion(InputStream stream) {
        try {
            var metaData = (Metadata) JAXB_UNMARSHALLER.unmarshal(stream);

            for (var snapshotVersion : metaData.versioning().snapshotVersions().snapshotVersion()) {
                if ((snapshotVersion.classifier() == null || snapshotVersion.classifier().isEmpty()) && snapshotVersion.extension().equals("jar")) {
                    return snapshotVersion.value();
                }
            }

            return "";
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
