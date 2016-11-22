package com.musicforall.web.profile;

import com.musicforall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(UserProfileController.class);

    @RequestMapping("/profile")
    public String profile(Model model) {

        model.addAttribute("user", SecurityUtil.currentUser().getUserData());
        return "profile";
    }
}