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

	private int counter = 0;
	public IndexController() {
		LOG.debug("Sample Debug Message");
	}

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("date", new Date());
		if (counter==0) this.addSong();
		counter++;
		return "index";
	}

	private void addDefaultUser(){
		userService.save(new User("JohnSmith26", "MyLoveIsNotU32", "JohnSmith@gmail.com"));
	}


	private void addDefaultAllSonglist(){
		addDefaultUser();
		Integer userId = userService.getIdByName("JohnSmith26");
		songlistService.save(userId, "All");
		songlistService.save(userId, "Not_all");
	}


	private void addSong(){
		addDefaultAllSonglist();
		Integer songlistId = songlistService.getSonglistId("All");
		songService.save("somePath", songlistId);
		songService.save("sonePath2", songlistId);
		Integer userId = userService.getIdByName("JohnSmith26");
		User u = userService.get(userId);
		Integer song_id = songService.getSongId("somePath");



		for (Songlist songlist:
				userService.getAllUserSonglist(u)) {
			LOG.info("Songl" + songlist.getName());
		}

		for (Song song: songlistService.getAllSongsInSonglist(songlistId)
			 ) {
			LOG.info("Song " + song.getName());
		}

	}

	private void getSongListSong()
	{


	}

}
