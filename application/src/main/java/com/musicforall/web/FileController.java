package com.musicforall.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Evgeniy on 11.06.2016.
 */

@Controller
public class FileController {
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFileHandler(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                File dir = new File(File.separator + "tmpFiles");
                dir.mkdirs();
                Files.copy(file.getInputStream(), Paths.get(dir.getAbsolutePath(), file.getOriginalFilename()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Hurray!";
        }
        return "File is empty";
    }

    @RequestMapping(value = "/getFile", method = RequestMethod.GET)
    public void getFileHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Stream<Path> walk = Files.walk(Paths.get("D:", "tmpFiles"), 2);
        List<Path> paths = walk
                .filter(path -> path.toString().toLowerCase().contains(".mp3"))
                .collect(Collectors.toList());
        if (paths.isEmpty()) return;

        try {
            Files.copy(paths.get(0), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/tryFiles", method = RequestMethod.GET)
    public String signUp() {
        return "tryFiles";
    }

}
