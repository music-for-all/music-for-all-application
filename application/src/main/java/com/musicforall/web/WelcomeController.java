package com.musicforall.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WelcomeController {

    private static final Logger LOG = LoggerFactory.getLogger(WelcomeController.class);

    public WelcomeController() {
        LOG.debug("Welcome controller");
    }

    @RequestMapping("/welcome")
    public String welcome(Model model, HttpServletRequest request) {
        LOG.debug("Requested /welcome");

        /* Check if there has been an authentication failure. */
        final Object exception = request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if (exception != null)
            model.addAttribute("SPRING_SECURITY_LAST_EXCEPTION", exception);

        model.addAttribute("request", request);
        return "welcome";
    }
}
