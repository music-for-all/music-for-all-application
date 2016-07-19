package com.musicforall.files.utils;

import java.io.File;
import java.nio.file.Paths;

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
}
