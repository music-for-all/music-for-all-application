package com.musicforall.web.file;

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
            trackJson.setSize(file.getSize());
            trackService.save(trackJson);
            return new ResponseEntity<String>("Song successfully saved", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/files/{id}/{partId}", method = RequestMethod.GET)
    public void getFileHandler(HttpServletResponse response, @PathVariable("id") Integer trackId,
                               @PathVariable("partId") int partId) {
        final Track track = trackService.get(trackId);
        final Optional<Path> filePath = Optional.ofNullable(manager.getFilePartById(track.getLocation(), partId));
        LOG.info(String.format("Streaming file: %s\n", track.getLocation()));

        if (filePath.isPresent()) {

            try {
                if (partId == FileManager.DEFAULT_CHUNK_ID) {
                    final User user = currentUser();
                    if (user != null) {
                        publisher.publishEvent(new TrackListenedEvent(trackId, user.getId()));
                    }
                }

                Files.copy(filePath.get(), response.getOutputStream());
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

    @RequestMapping(value = "/chunking", method = RequestMethod.GET)
    public String chunking() {
        return "chunking";
    }
}
