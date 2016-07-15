package com.musicforall.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SearchController {

    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    public SearchController() {
        LOG.debug("Search controller");
    }

    @RequestMapping("/search")
    public String welcome(Model model) {
        LOG.debug("Requested /search");
        return "search";
    }

    @RequestMapping(value = "/addSong", method = RequestMethod.POST)
    public String dummyAddSong(@RequestParam("songId") Integer id) {
        LOG.debug("Requested /addSong");

        return "search";
    }
}
