package com.musicforall.web.achievement;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.model.Achievement;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.services.AchievementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ENikolskiy.
 */
@RestController
@RequestMapping("/achievement")
public class AchievementController {
    @Autowired
    private AchievementsService achievementsService;

    private static final Playlist DUMMY_PLAYLIST = new Playlist();
    private static final Track DUMMY_TRACK = new Track();

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveAchievement(@RequestParam Achievement achievement,
                                          @RequestParam EventType type) {
        final boolean isValid = achievementsService.validateScript(achievement, prepareDummyVars(type));
        if (isValid) {
            achievementsService.save(achievement);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Script is invalid", HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> prepareDummyVars(EventType type) {
        final Map<String, Object> vars = new HashMap<>(1);
        if (type.isTrackEvent()) {
            vars.put("track", DUMMY_TRACK);
        } else if (type.isPlaylistEvent()) {
            vars.put("playlist", DUMMY_PLAYLIST);
        }
        return vars;
    }
}
