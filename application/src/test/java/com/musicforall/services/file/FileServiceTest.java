package com.musicforall.services.file;

import com.musicforall.files.manager.FileManager;
import com.musicforall.services.file.utils.FileTestUtils;
import com.musicforall.util.ServicesTestConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static com.musicforall.services.file.utils.FileTestUtils.getResource;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Paths.get;
import static org.junit.Assert.assertEquals;

/**
 * Created by Pavel Podgorniy on 9/11/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class FileServiceTest {

    private static final String TEST_FILE_NAME = "big_test_resource.mp3";

    private static final URL resourceUrl = getResource(FileService.class, TEST_FILE_NAME);

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceTest.class);
    private static File testDirectory;

    @Spy
    private static FileManager manager;

    @InjectMocks
    private static FileService fileService;

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
            final MockMultipartFile file = new MockMultipartFile("file", "sampleFile.mp3", null, inputStream);
            ResponseEntity<String> answerNewFile = fileService.checkFile(file);
            assertEquals(202, answerNewFile.getStatusCode().value());

            manager.save(file);
            ResponseEntity<String> answerExistedFile = fileService.checkFile(file);
            assertEquals(500, answerExistedFile.getStatusCode().value());

            MockMultipartFile file2 = new MockMultipartFile("testJunit", "name", null, IOUtils.toByteArray(""));
            ResponseEntity<String> answerEmptyFile = fileService.checkFile(file2);
            assertEquals(422, answerEmptyFile.getStatusCode().value());
        }
    }

}
