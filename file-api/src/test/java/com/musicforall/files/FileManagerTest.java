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
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.musicforall.files.utils.FileTestUtils.mergeFiles;
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
    public static final String BIG_TEST_FILE_NAME = "big_test_resource.mp3";

    private static final URL resourceUrl = FileManagerTest.class.getClassLoader().getResource("test_resource.jpg");
    private static final URL bigResourceUrl = FileManagerTest.class.getClassLoader().getResource(BIG_TEST_FILE_NAME);

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
    public void testSaveFile() throws Exception {
        try (InputStream inputStream = newInputStream(get(testDirectory.getAbsolutePath(), TEST_FILE_NAME))) {
            MockMultipartFile file = new MockMultipartFile("file", "saved.jpg", null, inputStream);
            assertNotNull(manager.save(file, false));
        }
    }

    @Test
    public void testGetFileByName() throws Exception {
        final String COPY_FILE = "copy.jpg";
        final File copy = get(testDirectory.getAbsolutePath(), COPY_FILE).toFile();
        try (FileOutputStream outputStream = new FileOutputStream(copy)) {
            final Path path = manager.getFilePathByName(TEST_FILE_NAME);
            copy(path, outputStream);
        }

        assertEquals(size(get(testDirectory.getAbsolutePath(), TEST_FILE_NAME)),
                size(get(testDirectory.getAbsolutePath(), COPY_FILE)));
    }

    @Test
    public void testSaveAlreadyExistingFile() throws Exception {
        try (InputStream inputStream = newInputStream(get(testDirectory.getAbsolutePath(), TEST_FILE_NAME))) {
            final MockMultipartFile file = new MockMultipartFile("file", "saveAlreadyExisted.jpg", null, inputStream);
            manager.save(file, false);
            assertNotNull(manager.save(file, false));
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
            assertNull(manager.save(file, false));
            verifyAll();
        }
    }

    @Test
    public void testSaveByUrl() throws Exception {
        final URL url = resourceUrl;
        assertNotNull(manager.save(url, false));
    }

    @Test
    public void saveByUrlWithIOException() throws Exception {
        final URL url = PowerMock.createMock(URL.class);
        EasyMock.expect(url.openStream()).andThrow(new IOException());
        replayAll();
        assertNull(manager.save(url, false));
        verifyAll();
    }

    @Test
    public void saveFileWithIOException() throws Exception {
        final MultipartFile file = PowerMock.createMock(MultipartFile.class);
        EasyMock.expect(file.getInputStream()).andThrow(new IOException());
        replayAll();
        assertNull(manager.save(file, false));
        verifyAll();
    }

    @Test
    public void testSplitAndSaveFile() throws Exception {
        final Path path = get(bigResourceUrl.toURI());
        try (InputStream inputStream = newInputStream(path)) {
            final MockMultipartFile file = new MockMultipartFile("file", BIG_TEST_FILE_NAME, null, inputStream);
            assertNotNull(manager.save(file, true));

            final File[] files = get(testDirectory.getAbsolutePath(), BIG_TEST_FILE_NAME).toFile().listFiles((dir, name) -> name.matches("\\d+"));
            long chunks = file.getSize() / FileManager.CHUNK_SIZE;
            long left = file.getSize() % FileManager.CHUNK_SIZE;
            assertEquals(left > 0 ? chunks + 1 : chunks, files.length);

            final File newFile = new File(testDirectory, "new music.mp3");
            mergeFiles(files, newFile);

            assertEquals(file.getSize(), newFile.length());
        }
    }
}