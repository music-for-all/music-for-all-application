package com.musicforall.web;

import com.musicforall.services.user.UserService;
import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    public IndexController() {
        LOG.info("");
    }

    @RequestMapping(value = {"/", "index"})
    public String index(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "index";
    }

    @RequestMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("username", SecurityUtil.currentUser().getUsername());
        return "profile";
    }
}