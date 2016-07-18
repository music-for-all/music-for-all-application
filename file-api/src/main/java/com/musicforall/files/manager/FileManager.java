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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Evgeniy on 11.06.2016.
 */

@Component
public class FileManager {
    private static final Logger LOG = LoggerFactory.getLogger(FileManager.class);

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

        if (!dir.exists() && !dir.mkdirs()) {
            throw new Error("Could not create the upload directory");
        }
    }

    /**
     * Copies the uploaded file to the upload directory.
     * @param file the file to be stored
     * @return <code>true</code> if the file has been saved successfully;
     * <code>false</code> otherwise.
     */
    public boolean save(final MultipartFile file) {
        final Path path = Paths.get(workingDirectory, file.getOriginalFilename());
        if (Files.exists(path)) {
            return false;
        }
        long savedBytes;
        InputStream in = null;
        try {
            in = file.getInputStream();
            savedBytes = Files.copy(in, path);
        } catch (IOException e) {
            LOG.error("Exception during file saving", e);
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                LOG.error(e.toString());
            }
        }
        return savedBytes == file.getSize();
    }

    /**
     * Converts the given filename to the Path containing the full path to the file.
     * @param filename the filename of the file
     * @return the path
     */
    public Path getFilePathByName(final String filename) {
        final Path path = Paths.get(workingDirectory, filename);
        if (!Files.exists(path)) {
            return null;
        }
        return path;
    }
}
