package pw.iwmc.libman;

import pw.iwmc.libman.api.objects.Dependency;

public class LibmanConstants {
    public static final Dependency ASM = Dependency.of("org.ow2.asm", "asm", "9.3");
    public static final Dependency ASM_COMMONS = Dependency.of("org.ow2.asm", "asm-commons", "9.3");
    public static final Dependency RELOCATOR = Dependency.of("me.lucko", "jar-relocator", "1.5");
}
