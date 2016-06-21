package com.musicforall.web;

import com.musicforall.model.Song;
import com.musicforall.model.Songlist;
import com.musicforall.model.Tag;
import com.musicforall.model.User;
import com.musicforall.services.SongService;
import com.musicforall.services.SonglistService;
import com.musicforall.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class IndexController {

	@Autowired
	private UserService userService;

	@Autowired
	private SonglistService songlistService;

	@Autowired
	private SongService songService;

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
