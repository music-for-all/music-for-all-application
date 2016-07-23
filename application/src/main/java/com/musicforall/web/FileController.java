package com.musicforall.web;

import com.musicforall.files.manager.FileManager;
import com.musicforall.history.handlers.events.TrackListenedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;

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

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFileHandler(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            final boolean saved = manager.save(file);
            return saved ? "success" : "error";
        }
        return "File is empty";
    }

    @RequestMapping(value = "/files/{fileName:.+}", method = RequestMethod.GET)
    public void getFileHandler(HttpServletResponse response, @PathVariable("fileName") String name) {
        final Optional<Path> filePath = Optional.of(manager.getFilePathByName(name));
        filePath.ifPresent(file -> {
            try {
                Files.copy(file, response.getOutputStream());

                Date date = new Date();
                int userId = 111;
                int trackId = 222;
                this.publisher.publishEvent(new TrackListenedEvent(trackId, date, userId));
            } catch (IOException e) {
                LOG.error("Streaming failed!", e);
            }
        });
    }

    @RequestMapping(value = "/tryFiles", method = RequestMethod.GET)
    public String signUp() {
        return "tryFiles";
    }
}
