package com.musicforall.services.file;

import com.musicforall.files.manager.FileManager;
import com.musicforall.model.Artist;
import com.musicforall.model.Track;
import com.musicforall.services.artist.ArtistService;
import com.musicforall.services.file.utils.FileTestUtils;
import com.musicforall.services.track.TrackService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static com.musicforall.services.file.utils.FileTestUtils.getResource;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Paths.get;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Pavel Podgorniy on 9/11/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    private static final String TEST_FILE_NAME = "big_test_resource.mp3";

    private static final int HTTP_STATUS_OK = 200;

    private static final int HTTP_STATUS_ACCEPTED = 202;

    private static final int HTTP_STATUS_INTERNAL_ERROR = 500;

    private static final int HTTP_STATUS_UNPROCESSABLE = 422;
    private static final URL resourceUrl = getResource(FileService.class, TEST_FILE_NAME);

    private static File testDirectory;

    @Spy
    private static FileManager manager;

    @Mock
    private static ArtistService artistService;

    @InjectMocks
    private static FileService fileService;

    @Mock
    private TrackService trackService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        testDirectory = FileTestUtils.createTestDirectory();
        manager = new FileManager();
        fileService = new FileServiceImpl();
        ReflectionTestUtils.setField(manager, "workingDirectory", testDirectory.getAbsolutePath());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        FileUtils.deleteDirectory(testDirectory);
    }

    @Test
    public void checkFileTest() throws Exception {
        try (InputStream inputStream = newInputStream(get(resourceUrl.toURI()))) {
            final MockMultipartFile file = new MockMultipartFile("file1", "sampleFile1.mp3", null, inputStream);
            assertEquals(HTTP_STATUS_ACCEPTED, fileService.checkFile(file).getStatusCode().value());

            manager.save(file);
            ResponseEntity<String> answerExistedFile = fileService.checkFile(file);
            assertEquals(HTTP_STATUS_INTERNAL_ERROR, answerExistedFile.getStatusCode().value());

            MockMultipartFile file2 = new MockMultipartFile("testJunit", "name", null, IOUtils.toByteArray(""));
            ResponseEntity<String> answerEmptyFile = fileService.checkFile(file2);
            assertEquals(HTTP_STATUS_UNPROCESSABLE, answerEmptyFile.getStatusCode().value());
        }
    }

    @Test
    public void uploadFileExistedArtistTest() throws Exception {
        try (InputStream inputStream = newInputStream(get(resourceUrl.toURI()))) {
            final MockMultipartFile file = new MockMultipartFile("file", "sampleFile.mp3", null, inputStream);
            final Track track = new Track("track", "title2", new Artist("artist2"), "album2", "/root/track2.mp3", null);

            when(artistService.get(any())).thenReturn((new Artist("artist")));
            when(trackService.save(any())).thenReturn(track);

            assertEquals(HTTP_STATUS_OK, fileService.uploadTrackFile(track, file).getStatusCode().value());
        }
    }

    @Test
    public void uploadFileNewArtistTest() throws Exception {
        try (InputStream inputStream = newInputStream(get(resourceUrl.toURI()))) {
            final MockMultipartFile file = new MockMultipartFile("file", "sampleFile.mp3", null, inputStream);
            final Track track = new Track("track", "title2", new Artist("artist2"), "album2", "/root/track2.mp3", null);

            when(artistService.get(any())).thenReturn((null));
            when(trackService.save(any())).thenReturn(track);

            assertEquals(HTTP_STATUS_OK, fileService.uploadTrackFile(track, file).getStatusCode().value());
        }
    }

}
