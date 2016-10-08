package com.musicforall.web.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SearchController {

    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @RequestMapping("/search")
    public String search(Model model) {
        return "search";
    }
}
