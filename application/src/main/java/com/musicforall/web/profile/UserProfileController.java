package com.musicforall.web.profile;

import com.musicforall.services.artist.ArtistService;
import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private ArtistService artistService;

    public UserProfileController() {
        LOG.info("");
    }

    @RequestMapping("/profile")
    public String profile(Model model) {

        model.addAttribute("user", SecurityUtil.currentUser());
        return "profile";
    }
}