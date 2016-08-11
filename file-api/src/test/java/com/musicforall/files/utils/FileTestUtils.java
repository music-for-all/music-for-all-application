package com.musicforall.files.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Evgeniy on 13.06.2016.
 */
public final class FileTestUtils {
    private FileTestUtils() {
    }

    public static File createTestDirectory() {
        final String projectDitPath = System.getProperty("user.dir");
        final File testDirectory = Paths.get(projectDitPath, "src", "test", "testDir").toFile();
        testDirectory.mkdirs();
        return testDirectory;
    }

    public static void mergeFiles(final File[] files, final File into) throws IOException {
        Arrays.sort(files);
        try (BufferedOutputStream mergingStream = new BufferedOutputStream(
                new FileOutputStream(into))) {
            for (final File f : files) {
                Files.copy(f.toPath(), mergingStream);
            }
        }
    }
}
