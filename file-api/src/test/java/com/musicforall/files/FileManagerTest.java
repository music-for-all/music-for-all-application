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
import java.util.Optional;

import static com.musicforall.files.utils.FileTestUtils.getResource;
import static com.musicforall.files.utils.FileTestUtils.mergeFiles;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Paths.get;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileManager.class)
public class FileManagerTest {

    private static final String TEST_FILE_NAME = "big_test_resource.mp3";

    private static final URL resourceUrl = getResource(FileManagerTest.class, TEST_FILE_NAME);

    private static final URL saveByUrlResource = getResource(FileManagerTest.class, "save_by_url_resource.jpg");

    private static File testTrackDirectory;

    private static File testPictureDirectory;

    private static FileManager manager;

    @BeforeClass
    public static void setUp() throws Exception {
        testTrackDirectory = FileTestUtils.createTestDirectory();
        testPictureDirectory = FileTestUtils.createTestDirectory();
        manager = new FileManager();
        ReflectionTestUtils.setField(manager, "musicDirectory", testTrackDirectory.getAbsolutePath());
        ReflectionTestUtils.setField(manager, "pictureDirectory", testPictureDirectory.getAbsolutePath());
        Files.copy(get(resourceUrl.toURI()), get(testTrackDirectory.getAbsolutePath(), TEST_FILE_NAME));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        FileUtils.deleteDirectory(testTrackDirectory);
        FileUtils.deleteDirectory(testPictureDirectory);
    }

    @Test
    public void testGetFileByName() throws Exception {
        final Optional<Path> path = manager.getFilePathByName(TEST_FILE_NAME);
        assertTrue(path.isPresent());
    }

    @Test
    public void testSaveAlreadyExistingTrack() throws Exception {
        try (InputStream inputStream = newInputStream(get(resourceUrl.toURI()))) {
            final MockMultipartFile file = new MockMultipartFile("file1", "saveAlreadyExistedMusic.jpg", null, inputStream);
            manager.saveTrack(file);
            assertNotNull(manager.saveTrack(file));
        }
    }

    @Test
    public void testSaveAlreadyExistingPicture() throws Exception {
        try (InputStream inputStream = newInputStream(get(resourceUrl.toURI()))) {
            final MockMultipartFile file = new MockMultipartFile("file2", "saveAlreadyExistedPic.jpg", null, inputStream);
            manager.savePicture(file);
            assertNotNull(manager.savePicture(file));
        }
    }

    @Test
    public void testGetNonExistingTrack() throws Exception {
        final Optional<Path> path = manager.getFilePathByName("fakeMusic.mp3");
        assertFalse(path.isPresent());
    }

    @Test
    public void testGetNonExistingPicture() throws Exception {
        final Optional<Path> path = manager.getPicturePathByName("fakePicture.jpg");
        assertFalse(path.isPresent());
    }

    @Test
    public void testSaveByUrl() throws Exception {
        final URL url = saveByUrlResource;
        assertTrue(manager.saveTrack(url).isPresent());
        assertTrue(manager.savePicture(url).isPresent());
    }

    @Test
    public void saveByUrlWithIOException() throws Exception {
        final URL url = PowerMock.createMock(URL.class);
        EasyMock.expect(url.openStream()).andThrow(new IOException());
        replayAll();
        assertFalse(manager.saveTrack(url).isPresent());
        verifyAll();
    }

    @Test
    public void saveTrackWithIOException() throws Exception {
        final MultipartFile file = PowerMock.createMock(MultipartFile.class);
        EasyMock.expect(file.getInputStream()).andThrow(new IOException());
        replayAll();
        assertFalse(manager.saveTrack(file).isPresent());
        verifyAll();
    }

    @Test
    public void testSplitAndSaveFile() throws Exception {
        final Path path = get(resourceUrl.toURI());
        try (InputStream inputStream = newInputStream(path)) {
            final MockMultipartFile file = new MockMultipartFile("file3", "test.mp3", null, inputStream);
            assertTrue(manager.saveTrack(file).isPresent());

            final File[] files = get(testTrackDirectory.getAbsolutePath(), "test.mp3")
                    .toFile()
                    .listFiles((dir, name) -> name.matches("\\d+"));
            long chunks = file.getSize() / FileManager.CHUNK_SIZE;
            long left = file.getSize() % FileManager.CHUNK_SIZE;
            assertEquals(left > 0 ? chunks + 1 : chunks, files.length);

            final File newFile = new File(testTrackDirectory, "new music.mp3");
            mergeFiles(files, newFile);

            assertEquals(file.getSize(), newFile.length());
        }
    }

    @Test
    public void testSavePictureFile() throws Exception {
        final Path path = get(resourceUrl.toURI());
        try (InputStream inputStream = newInputStream(path)) {
            final MockMultipartFile file = new MockMultipartFile("file4", "test.jpg", null, inputStream);
            assertTrue(manager.savePicture(file).isPresent());

            assertNotNull(manager.getPicturePathByName(file.getOriginalFilename()));
        }
    }

    @Test
    public void testGetFilePartById() throws Exception {
        final Path path = get(resourceUrl.toURI());
        try (InputStream inputStream = newInputStream(path)) {
            final MockMultipartFile file = new MockMultipartFile("file5", "testGetFilePartById.mp3", null, inputStream);
            manager.saveTrack(file);
        }
        assertTrue(manager.getFilePartById("testGetFilePartById.mp3", 0).isPresent());
    }
}