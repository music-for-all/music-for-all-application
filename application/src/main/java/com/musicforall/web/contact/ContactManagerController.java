package com.musicforall.web.contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ContactManagerController {

    private static final Logger LOG = LoggerFactory.getLogger(ContactManagerController.class);

    public ContactManagerController() {
        LOG.info("");
    }

    @RequestMapping("/contactManager")
    public String friends(Model model) {
        return "contactManager";
    }

}
