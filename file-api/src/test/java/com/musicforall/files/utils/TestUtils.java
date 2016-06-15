package com.musicforall.files.utils;

import java.io.File;
import java.nio.file.Paths;

/**
 * @author Evgeniy on 13.06.2016.
 */
public class TestUtils {
    public static File createTestDirectory() {
        String projectDitPath = System.getProperty("user.dir");
        String testDirPath = Paths.get(projectDitPath, "src", "test", "testDir").toString();
        File testDirectory = new File(testDirPath);
        testDirectory.mkdirs();
        return testDirectory;
    }
}
