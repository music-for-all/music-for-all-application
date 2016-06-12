package com.musicforall.files;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

public class FileManagerTest {

    private File testDirectory;
    private FileManager manager = new FileManager(File.separator + "test");

    @Before
    public void setUp() throws Exception {
        testDirectory = new File(File.separator + "test");
        testDirectory.mkdirs();
        Files.copy(this.getClass().getResourceAsStream("test_resource.jpg"), testDirectory.toPath());
    }

    @After
    public void tearDown() throws Exception {
        if (testDirectory.exists()) testDirectory.delete();
    }

    @Test
    public void testSaveMultipart() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", this.getClass().getResourceAsStream("test_resource.jpg"));

        boolean saved = manager.saveMultipart(file);

        Assert.assertTrue(saved);
    }

    @Test
    public void testStreamFileByName() throws Exception {
        manager.streamFileByName("test_resource.jpg",
                new FileOutputStream(testDirectory.getAbsolutePath() + File.separator + "1"));
    }
}