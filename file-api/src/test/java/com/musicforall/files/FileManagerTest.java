package com.musicforall.files;

import com.musicforall.files.manager.FileManager;
import com.musicforall.files.utils.FileTestUtils;
import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;
import static org.easymock.EasyMock.anyObject;
import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileManager.class)
public class FileManagerTest {

    public static final String TEST_FILE_NAME = "resource.jpg";

    private static final URL resourceUrl = FileManagerTest.class.getClassLoader().getResource("test_resource.jpg");

    private static File testDirectory;

    private static FileManager manager;

    @BeforeClass
    public static void setUp() throws Exception {
        testDirectory = FileTestUtils.createTestDirectory();
        manager = new FileManager();
        ReflectionTestUtils.setField(manager, "workingDirectory", testDirectory.getAbsolutePath());
        copy(get(resourceUrl.toURI()), get(testDirectory.getAbsolutePath(), TEST_FILE_NAME));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        FileUtils.deleteDirectory(testDirectory);
    }

    @Test
    public void testSave() throws Exception {
        try (InputStream inputStream = newInputStream(get(testDirectory.getAbsolutePath(), TEST_FILE_NAME))) {
            MockMultipartFile file = new MockMultipartFile("file", "saved.jpg", null, inputStream);
            assertTrue(manager.save(file));
        }
    }

    @Test
    public void testGetFileByName() throws Exception {
        final File copy = get(testDirectory.getAbsolutePath(), "copy.jpg").toFile();
        try (FileOutputStream outputStream = new FileOutputStream(copy)) {
            final Path path = manager.getFilePathByName(TEST_FILE_NAME);
            copy(path, outputStream);
        }

        assertEquals(size(get(testDirectory.getAbsolutePath(), TEST_FILE_NAME)),
                size(get(testDirectory.getAbsolutePath(), "copy.jpg")));
    }

    @Test
    public void testSaveToNull() throws Exception {
        try (InputStream inputStream = newInputStream(get(testDirectory.getAbsolutePath(), TEST_FILE_NAME))) {
            final MockMultipartFile file = new MockMultipartFile("file", null, null, inputStream);
            assertFalse(manager.save(file));
        }
    }

    @Test
    public void testSaveAlreadyExistingFile() throws Exception {
        try (InputStream inputStream = newInputStream(get(testDirectory.getAbsolutePath(), TEST_FILE_NAME))) {
            final MockMultipartFile file = new MockMultipartFile("file", "saveAlreadyExisted.jpg", null, inputStream);
            manager.save(file);
            assertFalse(manager.save(file));
        }
    }

    @Test
    public void testGetNonExistingFile() throws Exception {
        final Path path = manager.getFilePathByName("fakeResource.jpg");
        assertNull(path);
    }

    @Test
    public void testIOException() throws Exception {
        PowerMock.mockStatic(Files.class);

        EasyMock.expect(copy(anyObject(InputStream.class), anyObject(Path.class))).andThrow(new IOException());
        EasyMock.expect(exists(anyObject(Path.class))).andReturn(false);
        replayAll();

        final Path testFilePath = get(testDirectory.getAbsolutePath(), TEST_FILE_NAME);
        try (InputStream inputStream = new FileInputStream(testFilePath.toFile())) {
            final MockMultipartFile file = new MockMultipartFile("file", "saved.jpg", null, inputStream);
            assertFalse(manager.save(file));
            verifyAll();
        }
    }

    @Test
    public void testNonEqualSize() throws Exception {
        PowerMock.mockStatic(Files.class);

        EasyMock.expect(copy(anyObject(InputStream.class), anyObject(Path.class))).andReturn(-1L);
        EasyMock.expect(exists(anyObject(Path.class))).andReturn(false);
        replayAll();

        final Path testFilePath = get(testDirectory.getAbsolutePath(), TEST_FILE_NAME);
        try (InputStream inputStream = new FileInputStream(testFilePath.toFile())) {
            MockMultipartFile file = new MockMultipartFile("file", "saved.jpg", null, inputStream);
            assertFalse(manager.save(file));
            verifyAll();
        }
    }
}