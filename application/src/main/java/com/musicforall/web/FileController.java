package com.musicforall.web;

import com.musicforall.files.manager.FileManager;
import com.musicforall.history.handlers.events.TrackListenedEvent;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.services.track.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

import static com.musicforall.util.SecurityUtil.currentUser;


/**
 * @author Evgeniy on 11.06.2016.
 */

@Controller
public class FileController {
    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);
    public static final int STUB_TRACK_ID = 222;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private FileManager manager;

    @Autowired
    private TrackService trackService;

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> uploadFileHandler(
            @RequestPart("track") Track trackJson,
            @RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<String>("File is empty", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        final String filename = file.getOriginalFilename();
        if (manager.getFilePathByName(filename) != null) {
            return new ResponseEntity<String>("File exist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final Path saved = manager.save(file);
        if (saved != null) {
            trackJson.setLocation(filename);
            trackService.save(trackJson);
            return new ResponseEntity<String>("Song successfully saved", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/files/{fileName:.+}", method = RequestMethod.GET)
    public void getFileHandler(HttpServletResponse response, @PathVariable("fileName") String name) {
        final Optional<Path> filePath = Optional.ofNullable(manager.getFilePathByName(name));
        LOG.info(String.format("Streaming file: %s\n", name));

        if (filePath.isPresent()) {

            try {
                User user = currentUser();
                /* Set userId to 0 if no user is authenticated. */
                int userId = user != null ? user.getId() : 0;
                publisher.publishEvent(new TrackListenedEvent(STUB_TRACK_ID, userId));

                Files.copy(filePath.get(), response.getOutputStream());
            } catch (IOException e) {
                LOG.error("Streaming failed!", e);
            }
        } else {
            LOG.error("File not found");
        }
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public String signUp() {
        return "uploadFile";
    }

}
