package com.musicforall.files;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileOutputStream;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.size;
import static java.nio.file.Paths.get;

public class FileManagerTest {

    private File testDirectory;
    private FileManager manager;
    private final String testResourceName = "test_resource.jpg";

    @Before
    public void setUp() throws Exception {
        testDirectory = new File(File.separator + "test");
        testDirectory.mkdirs();
        manager = new FileManager(testDirectory.getAbsolutePath());
        copy(this.getClass().getResourceAsStream(testResourceName), get(testDirectory.getAbsolutePath(), testResourceName));
    }

    @After
    public void tearDown() throws Exception {
        if (testDirectory.exists()) testDirectory.delete();
    }

    @Test
    public void testSaveMultipart() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", this.getClass().getResourceAsStream(testResourceName));

        boolean saved = manager.saveMultipart(file);

        Assert.assertTrue(saved);
    }

    @Test
    public void testStreamFileByName() throws Exception {
        File copyFile = get(testDirectory.getAbsolutePath(), "copy.jpg").toFile();
        FileOutputStream outputStream = new FileOutputStream(copyFile);

        manager.streamFileByName(testResourceName, outputStream);

        Assert.assertEquals(size(get(testDirectory.getAbsolutePath(), testResourceName)), size(copyFile.toPath()));
    }
}