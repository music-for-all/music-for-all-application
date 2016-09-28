package com.musicforall.web.contact;

import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactManagerController {

    private static final Logger LOG = LoggerFactory.getLogger(ContactManagerController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private FollowerService followerService;

    public ContactManagerController() {
        LOG.info("");
    }

    @RequestMapping("/contactManager")
    public String friends(Model model) {
        return "contactManager";
    }

    @RequestMapping(value = "/showUser", method = RequestMethod.GET)
    public String userpage(@RequestParam("user_id") Integer user_id, Model model) {

        LOG.info("USER = " + userService.get(user_id).toString());
        LOG.info("PLAYLISTS = " + playlistService.getAllUserPlaylists(user_id).toString());
        LOG.info(" FOLOWERS " + followerService.getFollowersId(user_id).toString());
        LOG.info("FOLOWING" + followerService.getFollowingId(user_id).toString());

        model.addAttribute("user", userService.get(user_id));
        model.addAttribute("followers", followerService.getFollowersId(user_id));
        model.addAttribute("followings", followerService.getFollowingId(user_id));
        return "userpage";
    }
}
