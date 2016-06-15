package com.musicforall.files.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Evgeniy on 11.06.2016.
 */

@Component
public class FileManager {

    @Autowired
    @Qualifier("files")
    private String taleDirectory;
    private String workingDirectory;

    @PostConstruct
    private void prepareWorkingDirectory() {
        workingDirectory = System.getProperty("user.home") + File.separator + taleDirectory;
        File dir = new File(workingDirectory);
        dir.mkdirs();
    }

    public boolean save(final MultipartFile file) {
        long savedBytes = 0;
        try {
            savedBytes = Files.copy(file.getInputStream(), Paths.get(workingDirectory, file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedBytes == file.getSize();
    }

    public Path getFileByName(final String fileName) {
        Path path = Paths.get(workingDirectory, fileName);
        if (!Files.exists(path)) return null;

        return path;
    }
}
