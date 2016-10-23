package com.musicforall.web.achievement;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.model.Achievement;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.services.AchievementsService;
import com.musicforall.services.achievements.UserAchievementsService;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.musicforall.model.user.UserAchievement.Status.DONE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author ENikolskiy.
 */
@RestController
@RequestMapping("/achievements")
public class AchievementController {
    private static final Playlist DUMMY_PLAYLIST = Playlist.createDummyPlaylist();
    private static final Track DUMMY_TRACK = Track.createDummyTrack();
    @Autowired
    private AchievementsService achievementsService;
    @Autowired
    private UserAchievementsService userAchievementsService;

    @RequestMapping(method = POST)
    public ResponseEntity saveAchievement(@RequestBody Achievement achievement) {
        final boolean isValid = achievementsService.validateScript(achievement,
                prepareDummyVars(achievement.getEventType()));
        if (isValid) {
            achievementsService.save(achievement);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Script is invalid", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/user/current", method = GET)
    public ResponseEntity userAchievements() {
        final Integer userId = SecurityUtil.currentUserId();
        if (userId == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(userAchievementsService.getByUserIdInStatuses(userId, DONE), HttpStatus.OK);
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
