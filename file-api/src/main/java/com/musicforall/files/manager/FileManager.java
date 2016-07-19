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

import static java.util.Objects.requireNonNull;

/**
 * @author Evgeniy on 11.06.2016.
 */

@Component
public class FileManager {
    private static final Logger LOG = LoggerFactory.getLogger(FileManager.class);

    private static final String SAVE_ERROR_MSG = "Exception during file saving!";

    /**
     * The name of the directory which stores the uploaded files.
     */
    @Autowired
    @Qualifier("files")
    private String taleDirectory;

    /**
     * The absolute path to the upload directory.
     */
    private String workingDirectory;

    /**
     * Builds the absolute path to the upload directory;
     * Creates the directory if it does not exist.
     */
    @PostConstruct
    private void prepareWorkingDirectory() {
        workingDirectory = System.getProperty("user.home") + File.separator + taleDirectory;
        final File dir = new File(workingDirectory);
        dir.mkdirs();
    }

    public Path save(final MultipartFile file) {
        requireNonNull(file, "file must not be null");
        LOG.error("save file from multipart {}", file);
        Path path;
        try (InputStream in = file.getInputStream()) {
            path = save(in, file.getOriginalFilename());
        } catch (IOException e) {
            LOG.error(SAVE_ERROR_MSG, e);
            return null;
        }
        return path;
    }

    public Path save(final URL url) {
        requireNonNull(url, "url must not be null");
        LOG.info("save file by url {}", url);
        final String fileName = FilenameUtils.getName(url.toString());
        Path path;
        try (InputStream in = url.openStream()) {
            path = save(in, fileName);
        } catch (IOException e) {
            LOG.error(SAVE_ERROR_MSG, e);
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
            LOG.error(SAVE_ERROR_MSG, e);
            return null;
        }
        return path;
    }

    /**
     * Converts the given filename to the Path containing the full path to the file.
     * @param filename the filename of the file
     * @return the path
     */
    public Path getFilePathByName(final String filename) {
        requireNonNull(filename, "filename must not be null");
        final Path path = Paths.get(workingDirectory, filename);
        if (!Files.exists(path)) {
            return null;
        }
        return path;
    }
}
