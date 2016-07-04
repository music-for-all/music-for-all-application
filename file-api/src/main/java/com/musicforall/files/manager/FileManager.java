package com.musicforall.files.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(FileManager.class);

    @Autowired
    @Qualifier("files")
    private String taleDirectory;

    private String workingDirectory;

    @PostConstruct
    private void prepareWorkingDirectory() {
        workingDirectory = System.getProperty("user.home") + File.separator + taleDirectory;
        final File dir = new File(workingDirectory);
        dir.mkdirs();
    }

    public boolean save(final MultipartFile file) {
        final Path path = Paths.get(workingDirectory, file.getOriginalFilename());
        if (Files.exists(path)) {
            return false;
        }
        long savedBytes;
        try {
            savedBytes = Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            LOG.error("exception during file saving", e);
            return false;
        }
        return savedBytes == file.getSize();
    }

    public Path getFilePathByName(final String fileName) {
        final Path path = Paths.get(workingDirectory, fileName);
        if (!Files.exists(path)) {
            return null;
        }
        return path;
    }
}
