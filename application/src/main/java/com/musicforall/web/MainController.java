package com.musicforall.web;

import com.musicforall.model.Artist;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    private static final String MAIN = "main";

    @Autowired
    private TrackService trackService;

    @RequestMapping("/main")
    public String welcome(Model model) {
        final Set<Tag> tags1 = new HashSet<>(Arrays.asList(new Tag("tag1"), new Tag("tag2")));
        final Set<Tag> tags2 = new HashSet<>(Arrays.asList(new Tag("tag3"), new Tag("tag4")));

        Artist testArt1 = new Artist("artist", tags1);
        Artist testArt2=new Artist("artist", tags2);

        Track test_track1 = new Track("name", "title", testArt1, "album", "loc", tags1);
        Track test_track2 = new Track("name2","title2",testArt2,"album","loc",tags1);

        trackService.save(test_track1);
        trackService.save(test_track2);

        return MAIN;
    }
}