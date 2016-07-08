package com.musicforall.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicforall.files.manager.FileManager;
import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private TrackService trackService;

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadFileHandler(
            @RequestParam("track") String trackJson,
            @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<String>("File is empty", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        final String filename = file.getOriginalFilename();
        if (manager.getFilePathByName(filename) != null) {
            return new ResponseEntity<String>("File exist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final boolean saved = manager.save(file);
        if (saved) {
            final Track trackForAdding = parseJson(trackJson);
            trackForAdding.setLocation(filename);
            trackService.save(trackForAdding);
            return new ResponseEntity<String>("Song successfully saved", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/files/{fileName:.+}", method = RequestMethod.GET)
    public void getFileHandler(HttpServletResponse response, @PathVariable("fileName") String name) {
        final Optional<Path> filePath = Optional.of(manager.getFilePathByName(name));
        filePath.ifPresent(file -> {
                    try {
                        Files.copy(file, response.getOutputStream());
                    } catch (IOException e) {
                        LOG.error("Streaming failed!", e);
                    }
                }
        );
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String signUp() {
        return "uploadFile";
    }

    private Track parseJson(String trackJson) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        LOG.debug("Parsing Json");
        return mapper.readValue(trackJson, Track.class);
    }
}
