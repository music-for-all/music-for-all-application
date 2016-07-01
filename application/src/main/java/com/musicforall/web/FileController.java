package com.musicforall.web;

import com.musicforall.files.manager.FileManager;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.services.tag.TagService;
import com.musicforall.services.track.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

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

    @Autowired
    private TagService tagService;

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFileHandler(@RequestParam("file") MultipartFile file, @RequestParam("inputTitle") String title,
                             @RequestParam("inputArtist") String artist,
                             @RequestParam(value = "tags", required = false) Set<Tag> tags) {
        final String filename = file.getOriginalFilename();
        if (!file.isEmpty()) {
            boolean saved = manager.save(file);
            if (saved) {
                final String filepath = manager.getFilePathByName(filename).toString();
                //(Because track dosn't have constructor with Artist)
                // Track trackForAdding = new Track(artist, title, filepath);
                final Track trackForAdding = new Track(artist + " - " + title, filepath);

                for (final Tag tag : tags) {
                    if (!tagService.isTagExist(tag.getName())) {
                        tagService.save(tag.getName());
                    }
                    trackForAdding.addTags(tags);
                }
                trackService.save(trackForAdding);
            }
            return saved ? "Song successfully saved" : "Something wrong"; //fileApi have problem with returning status
        }
        return "File is empty";
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

    @RequestMapping(value = "/tryFiles", method = RequestMethod.GET)
    public String signUp() {
        return "tryFiles";
    }
}
