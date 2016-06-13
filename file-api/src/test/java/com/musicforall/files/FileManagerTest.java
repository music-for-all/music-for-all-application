package com.musicforall.files;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileManagerTest {

    private static File testDirectory;
    private static FileManager manager;
    private static final URL resourceUrl = FileManagerTest.class.getClassLoader().getResource("test_resource.jpg");


    @BeforeClass
    public static void setUp() throws Exception {
        String projectDitPath = System.getProperty("user.dir");
        String testDirPath = projectDitPath + File.separator + "src" + File.separator + "test" + File.separator + "for_test" + File.separator;
        testDirectory = new File(testDirPath);
        testDirectory.mkdirs();
        manager = new FileManager(testDirectory.getAbsolutePath());
        copy(get(resourceUrl.toURI()), get(testDirectory.getAbsolutePath(), "resource.jpg"));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        FileUtils.deleteDirectory(testDirectory);
    }

    @Test
    public void testSaveMultipart() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "saved.jpg", null, newInputStream(get(resourceUrl.toURI())));

        boolean saved = manager.saveMultipart(file);

        assertTrue(saved);
    }

    @Test
    public void testStreamFileByName() throws Exception {
        File copy = get(testDirectory.getAbsolutePath(), "copy.jpg").toFile();
        FileOutputStream outputStream = new FileOutputStream(copy);

        manager.streamFileByName("resource.jpg", outputStream);
        outputStream.flush();
        outputStream.close();

        assertEquals(size(get(testDirectory.getAbsolutePath(), "resource.jpg")),
                size(get(testDirectory.getAbsolutePath(), "copy.jpg")));
    }
}