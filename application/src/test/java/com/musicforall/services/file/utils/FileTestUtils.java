package com.musicforall.services.file.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import static org.apache.commons.io.FileUtils.cleanDirectory;

public final class FileTestUtils {
    private FileTestUtils() {
    }

    public static File createTestDirectory() throws IOException {
        final String projectDitPath = System.getProperty("user.dir");
        final File testDirectory = Paths.get(projectDitPath, "src", "test", "testDir").toFile();
        if (testDirectory.exists()) {
            cleanDirectory(testDirectory);
        }
        testDirectory.mkdirs();
        return testDirectory;
    }

    public static URL getResource(Class<?> clazz, final String resourceName) {
        return clazz.getClassLoader().getResource(resourceName);
    }
}
