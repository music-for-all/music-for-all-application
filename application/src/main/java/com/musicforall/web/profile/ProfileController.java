package com.musicforall.web.profile;

import com.musicforall.services.artist.ArtistService;
import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ArtistService artistService;

    public ProfileController() {
        LOG.info("");
    }

    @RequestMapping("/profile")
    public String profile(Model model) {

        model.addAttribute("user", SecurityUtil.currentUser());
        return "profile";
    }

    @RequestMapping("/artist/{name}")
    public String artistProfile(Model model, @PathVariable String name) {

        if (artistService.get(name) == null) {
            LOG.info(String.format("Artist %s not found", name));
            return "redirect:/";
        }

        model.addAttribute("artistName", name);
        return "artistProfile";
    }
}