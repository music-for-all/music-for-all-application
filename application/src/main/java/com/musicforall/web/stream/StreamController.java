package com.musicforall.web.stream;

import com.musicforall.common.cache.KeyValueRepository;
import com.musicforall.model.Track;
import com.musicforall.model.user.User;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author ENikolskiy.
 */
@RestController
@RequestMapping("/stream")
public class StreamController {

    @Autowired
    private KeyValueRepository<Integer, Track> cache;

    @Autowired
    private TrackService trackService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/track/{id}", method = POST)
    public ResponseEntity startStream(@PathVariable("id") Integer trackId) {
        final Integer userId = SecurityUtil.currentUserId();
        if (userId == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        final Track track = trackService.get(trackId);
        cache.put(userId, track);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/stop", method = POST)
    public ResponseEntity stopStream() {
        final Integer userId = SecurityUtil.currentUserId();
        if (userId == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        cache.remove(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = GET)
    public ResponseEntity getStreams(@RequestParam("ids[]") Collection<Integer> userIds) {
        final List<User> users = userService.getUsersById(userIds);
        final Map<Integer, Track> userToTrack = users.stream()
                .filter(u -> u.getConfig() != null && u.getConfig().isPublicRadio())
                .map(User::getId)
                .collect(HashMap::new, ((m, id) -> {
                    final Track track = cache.get(id);
                    if (track != null) {
                        m.put(id, track);
                    }
                }), HashMap::putAll);

        return new ResponseEntity<>(userToTrack, HttpStatus.OK);
    }
}
