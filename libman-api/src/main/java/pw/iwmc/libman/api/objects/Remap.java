package pw.iwmc.libman.api.objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A main of ${@link Remap}.
 */
public interface Remap {

    /**
     * Returns the source pattern to relocate.
     * @return source pattern.
     */
    String fromPattern();

    /**
     * Returns destination pattern.
     * @return destination pattern.
     */
    String toPattern();

    /**
     * Creates new remapping property.
     *
     * @param fromPattern source pattern.
     * @param toPattern destination pattern.
     *
     * @return the property.
     */
    @Contract("_, _ -> new")
    static @NotNull Remap of(String fromPattern, String toPattern) {
        return new RemapImpl(fromPattern, toPattern);
    }
}
