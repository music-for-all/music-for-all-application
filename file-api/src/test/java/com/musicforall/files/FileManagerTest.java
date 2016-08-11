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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.musicforall.files.utils.FileTestUtils.mergeFiles;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Paths.get;
import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileManager.class)
public class FileManagerTest {

    private static final String TEST_FILE_NAME = "big_test_resource.mp3";

    private static final URL resourceUrl = FileManagerTest.class.getClassLoader().getResource(TEST_FILE_NAME);

    private static final URL saveByUrlResource = FileManagerTest.class.getClassLoader().getResource("save_by_url_resource.jpg");

    private static File testDirectory;

    private static FileManager manager;

    @BeforeClass
    public static void setUp() throws Exception {
        testDirectory = FileTestUtils.createTestDirectory();
        manager = new FileManager();
        ReflectionTestUtils.setField(manager, "workingDirectory", testDirectory.getAbsolutePath());
        Files.copy(get(resourceUrl.toURI()), get(testDirectory.getAbsolutePath(), TEST_FILE_NAME));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        FileUtils.deleteDirectory(testDirectory);
    }

    @Test
    public void testGetFileByName() throws Exception {
        final Path path = manager.getFilePathByName(TEST_FILE_NAME);
        assertNotNull(path);
    }

    @Test
    public void testSaveAlreadyExistingFile() throws Exception {
        try (InputStream inputStream = newInputStream(get(resourceUrl.toURI()))) {
            final MockMultipartFile file = new MockMultipartFile("file", "saveAlreadyExisted.jpg", null, inputStream);
            manager.save(file);
            assertNotNull(manager.save(file));
        }
    }

    @Test
    public void testGetNonExistingFile() throws Exception {
        final Path path = manager.getFilePathByName("fakeResource.jpg");
        assertNull(path);
    }

    @Test
    public void testSaveByUrl() throws Exception {
        final URL url = saveByUrlResource;
        assertNotNull(manager.save(url));
    }

    @Test
    public void saveByUrlWithIOException() throws Exception {
        final URL url = PowerMock.createMock(URL.class);
        EasyMock.expect(url.openStream()).andThrow(new IOException());
        replayAll();
        assertNull(manager.save(url));
        verifyAll();
    }

    @Test
    public void saveFileWithIOException() throws Exception {
        final MultipartFile file = PowerMock.createMock(MultipartFile.class);
        EasyMock.expect(file.getInputStream()).andThrow(new IOException());
        replayAll();
        assertNull(manager.save(file));
        verifyAll();
    }

    @Test
    public void testSplitAndSaveFile() throws Exception {
        final Path path = get(resourceUrl.toURI());
        try (InputStream inputStream = newInputStream(path)) {
            final MockMultipartFile file = new MockMultipartFile("file", "test.mp3", null, inputStream);
            assertNotNull(manager.save(file));

            final File[] files = get(testDirectory.getAbsolutePath(), "test.mp3").toFile().listFiles((dir, name) -> name.matches("\\d+"));
            long chunks = file.getSize() / FileManager.CHUNK_SIZE;
            long left = file.getSize() % FileManager.CHUNK_SIZE;
            assertEquals(left > 0 ? chunks + 1 : chunks, files.length);

            final File newFile = new File(testDirectory, "new music.mp3");
            mergeFiles(files, newFile);

            assertEquals(file.getSize(), newFile.length());
        }
    }
}