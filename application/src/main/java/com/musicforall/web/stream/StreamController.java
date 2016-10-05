package com.musicforall.web.stream;

import com.musicforall.model.Track;
import com.musicforall.services.stream.StreamService;
import com.musicforall.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
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
    private StreamService streamService;

    @RequestMapping(value = "/track/{id}", method = POST)
    public ResponseEntity startStream(@PathVariable("id") Integer trackId) {
        return processWithCurrentUser(userId -> streamService.start(userId, trackId));
    }

    @RequestMapping(value = "/stop", method = POST)
    public ResponseEntity stopStream() {
        return processWithCurrentUser(streamService::stop);
    }

    @RequestMapping(value = "/publish/{toPublish}", method = POST)
    public ResponseEntity publishStream(@PathVariable("toPublish") boolean toPublish) {
        return processWithCurrentUser(userId -> streamService.publish(userId, toPublish));
    }

    @RequestMapping(method = GET)
    public ResponseEntity getStreams(@RequestParam("ids[]") Collection<Integer> userIds) {
        final Map<Integer, Track> userToTrack = streamService.getGroupedPublicStreams(userIds);
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
