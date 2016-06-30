package com.musicforall.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    private static final String MAIN = "main";

    public MainController() {
        LOG.debug("Main controller");
    }

    @RequestMapping("/main")
    public String welcome(Model model) {
        LOG.debug("Requested /main");
        return MAIN;
    }

    @RequestMapping(value = "/deleteSong", method = RequestMethod.POST)
    public String dummyDeleteSong(@RequestParam("deleteID") Integer id) {
        LOG.debug("Requested /deleteSong");

        return MAIN;
    }
}
