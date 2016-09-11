package com.musicforall.services.file;

import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Created by Pavel Podgorniy on 9/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Test
    public void checkFileTest() {
        MockMultipartFile file = new MockMultipartFile("b", "b.png", "image/png", "nonsensecontent".getBytes());
        ResponseEntity<String> responce = fileService.checkFile(file);

    }

}
