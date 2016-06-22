package com.musicforall.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicforall.model.Song;
import com.musicforall.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SearchController {

	private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

	public SearchController() {
		LOG.debug("Search controller");
	}

	@RequestMapping("/search")
	public String welcome(Model model) {
		LOG.debug("Requested /search");
		return "search";
	}

	@RequestMapping(value = "/searchQuery", method = RequestMethod.GET)
	@ResponseBody
	public Set<Song> dummyFind(@RequestParam("search") String search, @RequestParam("category") String jsonCategory) throws IOException {
		LOG.debug("Requested /searchQuery = " + search);
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> listCategorySearch = objectMapper.readValue(
				jsonCategory,
				objectMapper.getTypeFactory().constructCollectionType(
						List.class, String.class));

		Set<Song> array = new HashSet<>();
		Set<Tag> tag = new HashSet<>();
		String location = "/home/andrey/MusicForAll";
		Tag t = new Tag();
		t.setName("music");
		tag.add(t);
		array.add(new Song(tag, search, location));
		array.add(new Song(tag, "Nirvana", location));
		array.add(new Song(tag, "Disturbed", location));
		array.add(new Song(tag, "Rob Zombie", location));
		return array;
	}
}
