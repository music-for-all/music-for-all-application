package com.musicforall.files;

import com.musicforall.files.manager.FileManager;
import com.musicforall.files.utils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
import static org.junit.Assert.*;

public class FileManagerTest {

    private static final URL resourceUrl = FileManagerTest.class.getClassLoader().getResource("test_resource.jpg");
    private static File testDirectory;
    private static FileManager manager;

    @BeforeClass
    public static void setUp() throws Exception {
        testDirectory = TestUtils.createTestDirectory();
        manager = new FileManager();
        ReflectionTestUtils.setField(manager, "workingDirectory", testDirectory.getAbsolutePath());
        copy(get(resourceUrl.toURI()), get(testDirectory.getAbsolutePath(), "resource.jpg"));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        FileUtils.deleteDirectory(testDirectory);
    }

    @Test
    public void testSave() throws Exception {
        try (InputStream inputStream = newInputStream(get(testDirectory.getAbsolutePath(), "resource.jpg"))) {
            MockMultipartFile file = new MockMultipartFile("file", "saved.jpg", null, inputStream);
            assertTrue(manager.save(file));
        }
    }

    @Test
    public void testGetFileByName() throws Exception {
        File copy = get(testDirectory.getAbsolutePath(), "copy.jpg").toFile();
        try (FileOutputStream outputStream = new FileOutputStream(copy)) {
            Path path = manager.getFilePathByName("resource.jpg");
            copy(path, outputStream);
        }

        assertEquals(size(get(testDirectory.getAbsolutePath(), "resource.jpg")),
                size(get(testDirectory.getAbsolutePath(), "copy.jpg")));
    }

    @Test
    public void testSaveToNull() throws Exception {
        try (InputStream inputStream = newInputStream(get(testDirectory.getAbsolutePath(), "resource.jpg"))) {
            MockMultipartFile file = new MockMultipartFile("file", null, null, inputStream);
            assertFalse(manager.save(file));
        }
    }

    @Test
    public void testSaveAlreadyExistingFile() throws Exception {
        try (InputStream inputStream = newInputStream(get(testDirectory.getAbsolutePath(), "resource.jpg"))) {
            MockMultipartFile file = new MockMultipartFile("file", "saveAlreadyExisted.jpg", null, inputStream);
            manager.save(file);
            assertFalse(manager.save(file));
        }
    }

    @Test
    public void testGetNonExistingFile() throws Exception {
        Path path = manager.getFilePathByName("fakeResource.jpg");
        assertNull(path);
    }
}