package com.musicforall.web.social;

import com.musicforall.services.playlist.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class SocialController {

    @Autowired
    private PlaylistService playlistService;

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String userpage(@RequestParam("user_id") Integer user_id, Model model) {

        model.addAttribute("user_id", user_id);
        return "userpage";
    }
}
