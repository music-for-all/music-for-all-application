package com.musicforall.web.file;

import com.musicforall.common.Constants;
import com.musicforall.files.manager.FileManager;
import com.musicforall.model.Track;
import com.musicforall.services.file.FileService;
import com.musicforall.services.track.TrackService;
import com.musicforall.web.stream.RadioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;


/**
 * @author Evgeniy on 11.06.2016.
 */

@Controller
public class FileController {
    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileManager manager;

    @Autowired
    private RadioService radioService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadFileHandler(
            @RequestPart("track") Track trackJson,
            @RequestPart("file") MultipartFile file) {

        final ResponseEntity<String> errorStatus = fileService.checkFile(file);
        if (Objects.equals(errorStatus.getBody(), "ok")) {
            return fileService.uploadTrackFile(trackJson, file);
        } else {
            return errorStatus;
        }
    }

    @RequestMapping(value = "/files/{id}/{partId}", method = RequestMethod.GET)
    public void getFileHandler(HttpServletResponse response, @PathVariable(Constants.ID) Integer trackId,
                               @PathVariable("partId") int partId) {
        final Track track = trackService.get(trackId);
        final Optional<Path> filePath = manager.getFilePartById(track.getLocation(), partId);
        LOG.info(String.format("Streaming file: %s\n", track.getLocation()));

        if (filePath.isPresent()) {
            try {
                Files.copy(filePath.get(), response.getOutputStream());
                radioService.stream(track, partId);
            } catch (IOException e) {
                LOG.error("Streaming failed!", e);
            }
        } else {
            LOG.error("File not found");
        }
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String uploadFile() {
        return "uploadFile";
    }
}
