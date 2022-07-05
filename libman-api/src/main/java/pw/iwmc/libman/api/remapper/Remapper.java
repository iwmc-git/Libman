package pw.iwmc.libman.api.remapper;

import org.jetbrains.annotations.NotNull;
import pw.iwmc.libman.api.objects.Dependency;
import pw.iwmc.libman.api.objects.Remap;

import java.nio.file.Path;
import java.util.Collection;

public interface Remapper {
    void remap(@NotNull Dependency library, Collection<Remap> remapProperties);
}
