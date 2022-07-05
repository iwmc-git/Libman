package pw.iwmc.libman.api.remapper;

import org.jetbrains.annotations.NotNull;

import pw.iwmc.libman.api.objects.Dependency;
import pw.iwmc.libman.api.objects.Remap;

import java.util.Collection;

/**
 * Main of ${@link Remapper} interface.
 */
public interface Remapper {

    /**
     * Run remapping jar dependency.
     *
     * @param dependency the dependency for remapping.
     * @param remapProperties remapping rules.
     */
    void remap(@NotNull Dependency dependency, Collection<Remap> remapProperties);
}
