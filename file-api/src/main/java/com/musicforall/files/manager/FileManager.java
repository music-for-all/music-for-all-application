package com.musicforall.files.manager;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

    public Path save(final MultipartFile file) {
        Path path;
        try (InputStream in = file.getInputStream()) {
            path = save(in, file.getOriginalFilename());
        } catch (IOException e) {
            LOG.error("exception during file saving", e);
            return null;
        }
        return path;
    }

    public Path save(final URL url) {
        final String fileName = FilenameUtils.getName(url.toString());
        Path path;
        try (InputStream in = url.openStream()) {
            path = save(in, fileName);
        } catch (IOException e) {
            LOG.error("exception during file saving", e);
            return null;
        }
        return path;
    }

    private Path save(final InputStream stream, final String fileName) {
        final Path path = Paths.get(workingDirectory, fileName);
        if (Files.exists(path)) {
            return path;
        }
        try {
            Files.copy(stream, path);
        } catch (IOException e) {
            LOG.error("exception during file saving", e);
            return null;
        }
        return path;
    }

    public Path getFilePathByName(final String fileName) {
        final Path path = Paths.get(workingDirectory, fileName);
        if (!Files.exists(path)) {
            return null;
        }
        return path;
    }
}
