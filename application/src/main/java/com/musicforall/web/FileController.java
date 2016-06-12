package com.musicforall.web;

import com.musicforall.files.FileManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author Evgeniy on 11.06.2016.
 */

@Controller
public class FileController {

    private final String directory = File.separator + "Music";
    private final FileManager manager = new FileManager(directory);

    @PostConstruct
    private void prepareWorkingDirectory() {
        File dir = new File(directory);
        dir.mkdirs();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFileHandler(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            boolean saved = false;
            try {
                saved = manager.saveMultipart(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return saved ? "Hurray!" : "Oops!";
        }
        return "File is empty";
    }

    @RequestMapping(value = "/file/{name}", method = RequestMethod.GET)
    public void getFileHandler(HttpServletRequest request, HttpServletResponse response, @PathVariable("name") String name) {
        try {
            manager.streamFileByName(name, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/tryFiles", method = RequestMethod.GET)
    public String signUp() {
        return "tryFiles";
    }
}
