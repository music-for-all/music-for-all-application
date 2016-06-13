package com.musicforall.files;

import com.musicforall.files.manager.FileManager;
import com.musicforall.files.utils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileManagerTest {

    private final URL resourceUrl = FileManagerTest.class.getClassLoader().getResource("test_resource.jpg");
    private File testDirectory;
    private FileManager manager;

    @Before
    public void setUp() throws Exception {
        testDirectory = TestUtils.createTestDirectory();
        manager = new FileManager();
        ReflectionTestUtils.setField(manager, "workingDirectory", testDirectory.getAbsolutePath());
        copy(get(resourceUrl.toURI()), get(testDirectory.getAbsolutePath(), "resource.jpg"));
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(testDirectory);
    }

    @Test
    public void testSaveMultipart() throws Exception {
        boolean saved;
        try (InputStream inputStream = newInputStream(get(testDirectory.getAbsolutePath(), "resource.jpg"))) {
            MockMultipartFile file = new MockMultipartFile("file", "saved.jpg", null, inputStream);
            saved = manager.save(file);
        }

        assertTrue(saved);
    }

    @Test
    public void testStreamFileByName() throws Exception {
        File copy = get(testDirectory.getAbsolutePath(), "copy.jpg").toFile();
        try (FileOutputStream outputStream = new FileOutputStream(copy)) {
            Path path = manager.getFileByName("resource.jpg");
            copy(path, outputStream);
        }

        assertEquals(size(get(testDirectory.getAbsolutePath(), "resource.jpg")),
                size(get(testDirectory.getAbsolutePath(), "copy.jpg")));
    }
}