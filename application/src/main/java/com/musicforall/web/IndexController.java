package com.musicforall.web;

import com.musicforall.model.Song;
import com.musicforall.model.Songlist;
import com.musicforall.model.Tag;
import com.musicforall.model.User;
import com.musicforall.services.SongService;
import com.musicforall.services.SonglistService;
import com.musicforall.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    public IndexController() {
        LOG.debug("");
    }

    @RequestMapping(value = {"/", "index"})
    public String index() {
        return "index";
    }

    @RequestMapping("/profile")
    public String profile(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        model.addAttribute("userName", userDetails.getUsername());
        return "profile";
    }

    @RequestMapping("/login")
    public String login(Model model, CsrfToken csrf) {
        return "login";
    }
}
