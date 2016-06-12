package com.musicforall.files;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Evgeniy on 11.06.2016.
 */

public class FileManager {

    private String workingDirectory;

    public FileManager(final String directory) {
        workingDirectory = directory;
    }

    public boolean saveMultipart(final MultipartFile file) throws IOException {
        long savedBytes = Files.copy(file.getInputStream(), Paths.get(workingDirectory, file.getOriginalFilename()));
        return savedBytes == file.getSize();
    }

    public void streamFileByName(final String name, final OutputStream destination) throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(workingDirectory), 2);
        List<Path> paths = walk
                .filter(path -> path.toString().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        if (paths.isEmpty()) return;

        Files.copy(paths.get(0), destination);
    }
}
