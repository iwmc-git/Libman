package pw.iwmc.libman.api.objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Remap {
    String fromPattern();
    String toPattern();

    @Contract("_, _ -> new")
    static @NotNull Remap of(String fromPattern, String toPattern) {
        return new RemapImpl(fromPattern, toPattern);
    }
}
