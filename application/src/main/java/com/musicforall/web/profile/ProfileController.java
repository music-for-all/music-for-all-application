package com.musicforall.web.profile;

import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
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

        Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
        if (connection == null) {
            System.err.printf("Facebook Connection: %s\n", connection);
            return "redirect:/auth/facebook";
        }

        model.addAttribute("facebookProfile", connection.getApi().userOperations().getUserProfile());
        model.addAttribute("username", SecurityUtil.currentUser().getUsername());

        return "profile";
    }
}