package pw.iwmc.libman.remapper;

import org.jetbrains.annotations.NotNull;

import pw.iwmc.libman.Libman;
import pw.iwmc.libman.api.objects.Dependency;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IsolatedClassLoader extends URLClassLoader {
    public static final Map<List<Dependency>, IsolatedClassLoader> LOADERS = new HashMap<>();

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public IsolatedClassLoader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader().getParent());
    }

    public static @NotNull IsolatedClassLoader obtainClassLoader(@NotNull List<Dependency> dependencies) {
        for (var library : dependencies) {
            if (!Libman.libman().downloaded().containsKey(library)) {
                throw new IllegalStateException("Library " + library.artifactId() + " is not loaded.");
            }
        }

        synchronized (LOADERS) {
            var classLoader = LOADERS.get(dependencies);

            if (classLoader != null) {
                return classLoader;
            }

            var urls = dependencies.stream()
                    .map(Libman.libman().downloaded()::get)
                    .map(file -> {
                        try {
                            return file.toUri().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new);

            classLoader = new IsolatedClassLoader(urls);
            LOADERS.put(dependencies, classLoader);

            return classLoader;
        }
    }
}