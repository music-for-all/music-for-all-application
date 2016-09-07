package com.musicforall.web.profile;

import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

@Controller
public class ProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);

    @Inject
    private ConnectionRepository connectionRepository;

    public ProfileController() {
        LOG.info("");
    }

    @RequestMapping("/profile")
    public String profile(Model model) {

        model.addAttribute("user", SecurityUtil.currentUser());

        return "profile";
    }
}