package com.musicforall.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class IndexController {

	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

	public IndexController() {
		LOG.debug("");
	}

	@RequestMapping(value = {"/", "index"})
	public String index(Model model, CsrfToken csrf) {

		model.addAttribute("date", new Date());
		model.addAttribute("_csrf", csrf);
		return "index";
	}

	@RequestMapping("/profile")
	public String profile(Model model, CsrfToken csrf) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();

		model.addAttribute("userName", userDetail.getUsername());
		model.addAttribute("_csrf", csrf);
		return "profile";
	}

	@RequestMapping("/login")
	public String login(Model model, CsrfToken csrf) {

		model.addAttribute("_csrf", csrf);
		return "login";
	}
}
