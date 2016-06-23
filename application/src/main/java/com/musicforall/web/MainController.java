package com.musicforall.web;

import com.musicforall.model.Songlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
public class MainController {

	private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

	public MainController() {
		LOG.debug("Main controller");
	}

	@RequestMapping("/main")
	public String welcome(Model model) {
		LOG.debug("Requested /main");
		return "main";
	}

	private static Set<Songlist> set;
	private static int id = 0;

	@RequestMapping(value = "/getPlayLists", method = RequestMethod.GET)
	@ResponseBody
	public Set<Songlist> dummyGetPlayLists() {
		LOG.debug("Requested /getPlayLists");

		if (set == null) {
			set = new HashSet<>();
			Songlist songlist = new Songlist(id++);
			songlist.setName("Nirvana");
			set.add(songlist);
			songlist = new Songlist(id++);
			songlist.setName("Disturbed");
			set.add(songlist);
			songlist = new Songlist(id++);
			songlist.setName("Rob Zombie");
			set.add(songlist);
		}
		return set;
	}

	@RequestMapping(value = "/addPlaylist", method = RequestMethod.POST)
	public String dummyAddPlaylist(@RequestParam("playlist") String name) {
		LOG.debug("Requested /addPlaylist = " + name);
		if (set != null){
			Songlist songlist = new Songlist(id++);
			songlist.setName(name);
			set.add(songlist);
		}
		return "main";
	}

	@RequestMapping(value = "/deletePlaylist", method = RequestMethod.POST)
	public String dummyDeletePlaylist(@RequestParam("deleteID") String id) {
		LOG.debug("Requested /deletePlaylist = " + id);
		if (set != null){
			Songlist songlist = new Songlist(this.id++);
			songlist.setName(id + " deleted");
			set.add(songlist);
		}
		return "main";
	}
}
