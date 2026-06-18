package org.momtsim.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ResourceUtils {
    private ResourceUtils() {
    }

    public static InputStream open(String location) throws IOException {
        if (location == null || location.trim().isEmpty()) {
            throw new FileNotFoundException("resource location must not be empty");
        }

        Path path = Paths.get(location);
        if (Files.exists(path)) {
            return Files.newInputStream(path);
        }

        String resourceName = normalizeResourceName(location);
        InputStream inputStream = openClasspathResource(resourceName);
        if (inputStream != null) {
            return inputStream;
        }

        throw new FileNotFoundException(String.format("unable to find file or classpath resource: %s", location));
    }

    private static InputStream openClasspathResource(String resourceName) {
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        if (contextLoader != null) {
            InputStream inputStream = contextLoader.getResourceAsStream(resourceName);
            if (inputStream != null) {
                return inputStream;
            }
        }
        return ResourceUtils.class.getClassLoader().getResourceAsStream(resourceName);
    }

    private static String normalizeResourceName(String location) {
        String resourceName = location.replace('\\', '/');
        while (resourceName.startsWith("./")) {
            resourceName = resourceName.substring(2);
        }
        while (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }
        return resourceName;
    }
}
