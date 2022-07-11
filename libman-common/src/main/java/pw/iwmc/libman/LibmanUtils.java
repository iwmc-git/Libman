package pw.iwmc.libman;

import org.jetbrains.annotations.NotNull;

import pw.iwmc.libman.api.objects.Dependency;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;

public class LibmanUtils {

    public static String stringFromStream(@NotNull InputStream stream) {
        try {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull File libraryFile(@NotNull Dependency dependency) {
        return libraryPath(dependency).toFile();
    }

    public static @NotNull File libraryFile(@NotNull Dependency dependency, @NotNull Path root) {
        return libraryPath(dependency, root).toFile();
    }

    public static @NotNull Path libraryPath(@NotNull Dependency dependency) {
        return Paths.get(dependency.groupId(), dependency.artifactId(), dependency.artifactName() + ".jar");
    }

    public static @NotNull Path libraryPath(@NotNull Dependency dependency, @NotNull Path root) {
        return root.resolve(libraryPath(dependency));
    }

    public static @NotNull URL sha1Url(@NotNull Dependency dependency, String root) {
        try {
            var uri = baseUrl(dependency, root).toURI();
            var path = uri.getPath() + "/" + URLEncoder.encode(dependency.artifactName(), StandardCharsets.UTF_8) + ".jar.sha1";

            return uri.resolve(path.replace("//", "/")).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Cannot create artifact base url for: " + dependency.artifactId(), e);
        }
    }

    public static @NotNull URL baseUrl(@NotNull Dependency dependency, @NotNull String root) {
        try {
            var uri = new URL(root).toURI();

            var path = String.join(
                    "/", uri.getPath(),
                    dependency.groupId().replace(".", "/"),
                    dependency.artifactId().replace(".", "/"),
                    URLEncoder.encode(dependency.version(), StandardCharsets.UTF_8)
            );

            return uri.resolve(path.replace("//", "/")).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Cannot create base url for: " + dependency.artifactId(), e);
        }
    }

    public static @NotNull URL artifactUrl(@NotNull Dependency dependency, @NotNull String root) {
        try {
            var uri = baseUrl(dependency, root).toURI();
            var path = uri.getPath() + "/" + URLEncoder.encode(dependency.artifactName(), StandardCharsets.UTF_8) + ".jar";

            return uri.resolve(path.replace("//", "/")).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Cannot create artifact base url for: " + dependency.artifactId(), e);
        }
    }

    public static @NotNull URL metadataUrl(@NotNull Dependency dependency, @NotNull String root) {
        try {
            var uri = baseUrl(dependency, root).toURI();
            var path = uri.getPath() + "/maven-metadata.xml";

            return uri.resolve(path.replace("//", "/")).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Cannot create maven-metadata base url for: " + dependency.artifactId(), e);
        }
    }

    public static @NotNull File metadataFile(@NotNull Dependency dependency, @NotNull String root) {
        var replacedGroupId = dependency.groupId().replace(".", "/");
        var replacedArtifactId = dependency.artifactId().replace(".", "/");
        var version = dependency.version();

        return Paths.get(root).resolve(replacedGroupId).resolve(replacedArtifactId).resolve(version).resolve("maven-metadata.xml").toFile();
    }

    public static void makeDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            if (!Files.isDirectory(path)) {
                Files.delete(path);
            }
        } else {
            Files.createDirectories(path);
        }
    }


    public static boolean check(File file, String hash) {
        try {
            var code = fileHash(file);
            return hash.equalsIgnoreCase(code);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static @NotNull String fileHash(File file) throws Exception {
        var digest = MessageDigest.getInstance("SHA-1");
        var inputStream = new FileInputStream(file);

        var byteArray = new byte[1024];
        var bytesCount = 0;

        while ((bytesCount = inputStream.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        inputStream.close();

        var bytes = digest.digest();
        var stringBuilder = new StringBuilder();

        for (var b : bytes) {
            stringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuilder.toString();
    }

    public static @NotNull String bytesToHexString(byte @NotNull [] bytes) {
        var stringBuilder = new StringBuilder();
        for (var b : bytes) {
            var value = b & 0xFF;

            if (value < 16) {
                stringBuilder.append("0");
            }

            stringBuilder.append(Integer.toHexString(value).toUpperCase());
        }

        return stringBuilder.toString();
    }
}
