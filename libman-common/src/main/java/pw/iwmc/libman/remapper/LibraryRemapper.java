package pw.iwmc.libman.remapper;

import org.jetbrains.annotations.NotNull;

import pw.iwmc.libman.Libman;
import pw.iwmc.libman.LibmanConstants;
import pw.iwmc.libman.LibmanUtils;
import pw.iwmc.libman.api.objects.Dependency;
import pw.iwmc.libman.api.objects.Remap;
import pw.iwmc.libman.api.remapper.Remapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LibraryRemapper implements Remapper {
    private final Libman libman = Libman.libman();

    private final Class<?> classJarRelocator;
    private final Class<?> classReloation;

    public LibraryRemapper() {
        libman.log("Initializing remapper...");

        var dependencies = List.of(LibmanConstants.ASM, LibmanConstants.ASM_COMMONS, LibmanConstants.RELOCATOR);
        var isolatedClassLoader = IsolatedClassLoader.obtainClassLoader(dependencies);

        Class<?> classJarRelocator;
        Class<?> classRelocation;

        try {
            classJarRelocator = isolatedClassLoader.loadClass("me.lucko.jarrelocator.JarRelocator");
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Could not found class me.lucko.jarrelocator.JarRelocator", e);
        }

        try {
            classRelocation = isolatedClassLoader.loadClass("me.lucko.jarrelocator.Relocation");
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Could not found class me.lucko.jarrelocator.Relocation", e);
        }

        this.classJarRelocator = classJarRelocator;
        this.classReloation = classRelocation;
    }

    @Override
    public void remap(@NotNull Dependency dependency, Collection<Remap> remapProperties) {
        var rules = new LinkedList<>();

        var from = libman.downloadedDependsFolder();
        var to = libman.remappedDependsFolder();

        var input = from.resolve(LibmanUtils.libraryPath(dependency, from)).toFile();
        var output = to.resolve(LibmanUtils.libraryPath(dependency, to)).toFile();

        // If there are no parameters for remapping,
        // but I really want to download this dependency.
        if (remapProperties == null || remapProperties.isEmpty()) {
            libman.log(String.format("Dependency %s has no remap rules, skipping...", dependency.artifactName()));
            libman.remapped().put(dependency, input.toPath());
            return;
        }

        if (!output.exists()) {
            libman.log(String.format("Dependency %s begins remapping... (%s rules)", dependency.artifactName(), remapProperties.size()));
            var currentTime = System.currentTimeMillis();

            try {
                for (var property : remapProperties) {
                    var constructor = classReloation.getConstructor(String.class, String.class);
                    var instance = constructor.newInstance(property.fromPattern(), property.toPattern());
                    rules.add(instance);
                }
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException operationException) {
                throw new IllegalArgumentException("Cannot instantiate Relocation class", operationException);
            }

            try {
                LibmanUtils.makeDirectory(output.getParentFile().toPath());
            } catch (IOException exception) {
                throw new IllegalArgumentException("Cannot create output folder", exception);
            }

            try {
                var constructor = classJarRelocator.getConstructor(File.class, File.class, Collection.class);
                var instance = constructor.newInstance(input, output, rules);
                var run = classJarRelocator.getMethod("run");

                if (Files.notExists(to)) {
                    Files.createDirectories(output.toPath());
                }

                run.invoke(instance);

                var endTime = System.currentTimeMillis() - currentTime;

                libman.log(String.format("Dependency %s successfully remapped! (%sms took)", dependency.artifactName(), endTime));
                libman.remapped().put(dependency, output.toPath());
            } catch (Exception exception) {
                throw new IllegalArgumentException("Cannot instantiate JarRelocator class", exception);
            }
        }
    }
}
