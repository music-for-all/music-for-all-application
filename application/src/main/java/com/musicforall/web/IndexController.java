package com.musicforall.web;

import com.musicforall.model.User;
import com.musicforall.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    public IndexController() {
        LOG.debug("");
    }

    @RequestMapping(value = {"/", "index"})
    public String index(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "index";
    }

    @RequestMapping("/profile")
    public String profile(Model model) {

        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username", user.getUsername());
        return "profile";
    }
}
