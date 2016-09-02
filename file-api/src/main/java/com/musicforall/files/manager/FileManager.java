package com.musicforall.files.manager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.Files.*;
import static java.util.Objects.requireNonNull;

/**
 * @author Evgeniy on 11.06.2016.
 */

@Component
public class FileManager {
    public static final int CHUNK_SIZE = 500_000;
    public static final int DEFAULT_CHUNK_ID = 0;
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

    public Optional<Path> save(final MultipartFile file) {
        requireNonNull(file, "file must not be null");
        LOG.info("save file from multipart {}", file);
        try (InputStream in = file.getInputStream()) {
            return Optional.of(save(in, file.getOriginalFilename()));
        } catch (IOException e) {
            LOG.error(SAVE_ERROR_MSG, e);
        }
        return Optional.empty();
    }

    public Optional<Path> save(final URL url) {
        requireNonNull(url, "url must not be null");
        LOG.info("save file by url {}", url);
        final String fileName = FilenameUtils.getName(url.toString());
        try (InputStream in = url.openStream()) {
            return Optional.of(save(in, fileName));
        } catch (IOException e) {
            LOG.error(SAVE_ERROR_MSG, e);
        }
        return Optional.empty();
    }

    private Path save(final InputStream stream, final String fileName) throws IOException {
        final Path path = Paths.get(workingDirectory, fileName);
        if (isDirectory(path)) {
            return path;
        } else {
            createDirectory(path);
        }
        return splitAndSave(stream, path);
    }

    private Path splitAndSave(final InputStream stream, final Path dirPath) throws IOException {
        final Path tmp = createTempFile(dirPath, null, null);
        try (BufferedInputStream in = new BufferedInputStream(stream);
             OutputStream out = newOutputStream(tmp)) {

            IOUtils.copy(in, out);
            splitFile(tmp, dirPath);
        }
        FileUtils.deleteQuietly(tmp.toFile());
        return dirPath;
    }

    private void splitFile(final Path filePath, final Path dirPath) throws IOException {
        int partCounter = DEFAULT_CHUNK_ID;
        final byte[] buffer = new byte[CHUNK_SIZE];
        try (BufferedInputStream bis = new BufferedInputStream(newInputStream(filePath))) {
            int length = bis.read(buffer);
            while (length > 0) {
                final Path chunkPath = dirPath.resolve(createChunkName(partCounter++));
                try (OutputStream out = newOutputStream(chunkPath)) {
                    out.write(buffer, 0, length);
                }
                length = bis.read(buffer);
            }
        }
    }

    /**
     * Converts the given filename to the Path containing the full path to the file.
     *
     * @param filename the filename of the file
     * @return the path
     */
    public Optional<Path> getFilePathByName(final String filename) {
        requireNonNull(filename, "filename must not be null");
        final Path path = Paths.get(workingDirectory, filename);
        if (!exists(path)) {
            return Optional.empty();
        }
        return Optional.of(path);
    }

    public Optional<Path> getFilePartById(final String location, final int partId) {
        requireNonNull(location, "location must not be null");
        final String fileName = location + File.separator + createChunkName(partId);
        return getFilePathByName(fileName);
    }

    private String createChunkName(int counter) {
        return String.format("%04d", counter);
    }

    /**
     * need this only when application is run for first time to be sure
     * that directory with files doesn't contain old files
     *
     * SHOULD NOT BE USED ANYWHERE IN APP!!! ONLY FOR POPULATOR SERVICES
     */

    public void clearDirectory() {
        final Path path = Paths.get(workingDirectory);
        try {
            FileUtils.cleanDirectory(path.toFile());
        } catch (IOException e) {
            LOG.error("Exception during directory cleaning! Clean directory manually!");
        }
    }
}
