package com.musicforall.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class IndexController {

	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

	public IndexController() {
		LOG.debug("Sample Debug Message");
	}

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("date", new Date());
		return "index";
	}
}
