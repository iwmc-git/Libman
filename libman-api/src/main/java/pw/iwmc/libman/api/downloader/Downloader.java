package pw.iwmc.libman.api.downloader;

import org.jetbrains.annotations.NotNull;
import pw.iwmc.libman.api.objects.Dependency;

public interface Downloader {
    void downloadDependency(@NotNull Dependency dependency);
}
