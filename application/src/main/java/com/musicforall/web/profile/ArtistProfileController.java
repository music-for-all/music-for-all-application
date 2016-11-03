package com.musicforall.web.profile;

import com.musicforall.model.Artist;
import com.musicforall.services.artist.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ArtistProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(ArtistProfileController.class);

    @Autowired
    private ArtistService artistService;

    public ArtistProfileController() {
        LOG.info("");
    }

    @RequestMapping("/artist/{id}")
    public String profile(Model model, @PathVariable Integer id) {

        Artist artist = artistService.get(id);
        if (artist  == null) {
            return "redirect:/";
        }

        model.addAttribute("artistName", artist.getName());
        return "artistProfile";
    }
}