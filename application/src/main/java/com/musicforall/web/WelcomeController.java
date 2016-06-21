package com.musicforall.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class WelcomeController {

	private static final Logger LOG = LoggerFactory.getLogger(WelcomeController.class);

	public WelcomeController() {
		LOG.debug("Welcome controller");
	}

	@RequestMapping("/welcome")
	public String welcome(Model model) {
		LOG.debug("Requested /welcome");
		return "welcome";
	}
}
