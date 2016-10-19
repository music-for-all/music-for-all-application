package com.musicforall.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Evgeniy on 19.10.2016.
 */
@Controller
public class NavigationController {

    @RequestMapping("/admin/achievements")
    public String search(Model model) {
        return "createAchievement";
    }
}
