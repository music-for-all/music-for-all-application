package com.musicforall.files.utils;

import java.io.File;
import java.nio.file.Paths;

/**
 * @author Evgeniy on 13.06.2016.
 */
public final class FileTestUtils {
    public static File createTestDirectory() {
        final String projectDitPath = System.getProperty("user.dir");
        final String testDirPath = Paths.get(projectDitPath, "src", "test", "testDir").toString();
        final File testDirectory = new File(testDirPath);
        testDirectory.mkdirs();
        return testDirectory;
    }

    private FileTestUtils() {
    }
}
