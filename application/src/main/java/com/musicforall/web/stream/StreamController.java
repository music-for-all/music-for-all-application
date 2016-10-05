package com.musicforall.web.stream;

import com.musicforall.common.cache.KeyValueRepository;
import com.musicforall.model.Track;
import com.musicforall.model.user.User;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.function.Consumer;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author ENikolskiy.
 */
@RestController
@RequestMapping("/stream")
public class StreamController {

    @Autowired
    @Qualifier("guava")
    private KeyValueRepository<Integer, Track> cache;
    @Autowired
    private TrackService trackService;
    @Autowired
    private UserService userService;
    @Autowired
    private RadioService radioService;

    @RequestMapping(value = "/track/{id}", method = POST)
    public ResponseEntity startStream(@PathVariable("id") Integer trackId) {
        return processWithCurrentUser(userId -> {
            final Track track = trackService.get(trackId);
            cache.put(userId, track);
        });
    }

    @RequestMapping(value = "/stop", method = POST)
    public ResponseEntity stopStream() {
        radioService.stream(null, -1);
        return processWithCurrentUser(cache::remove);
    }

    @RequestMapping(value = "/publish/{toPublish}", method = POST)
    public ResponseEntity publishStream(final @PathVariable("toPublish") boolean toPublish) {
        return processWithCurrentUser(userId -> {
            final User currentUser = userService.getWithSettingsById(userId);
            currentUser.getSettings().setPublicRadio(toPublish);
            userService.save(currentUser);
        });
    }

    @RequestMapping(method = GET)
    public ResponseEntity getStreams(@RequestParam("ids[]") Collection<Integer> userIds) {
        final List<User> users = userService.getAllWithSettingsByIds(userIds);
        final Map<Integer, Track> userToTrack = users.stream()
                .filter(u -> u.getSettings() != null && u.getSettings().isPublicRadio())
                .map(User::getId)
                .collect(HashMap::new, ((m, id) -> {
                    final Track track = cache.get(id);
                    if (track != null) {
                        m.put(id, track);
                    }
                }), HashMap::putAll);

        return new ResponseEntity<>(userToTrack, HttpStatus.OK);
    }

    private ResponseEntity processWithCurrentUser(Consumer<Integer> consumer) {
        final Integer currentUserId = SecurityUtil.currentUserId();
        if (currentUserId == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        consumer.accept(currentUserId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
