package com.musicforall.web.stream;

import com.musicforall.model.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.musicforall.util.SecurityUtil.currentUserId;

/**
 * @author ENikolskiy.
 */
@Service
public class WebRadio implements Radio {

    public static final String SUBSCRIPTION_URL = "/radio/subscribers/";

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void stream(Track track, int partId) {
        stream(currentUserId(), track, partId);
    }

    @Override
    public void stream(Integer userId, Track track, int partId) {
        final Map<String, Object> map = new HashMap<>();
        map.put("track", track);
        map.put("partId", partId);

        template.convertAndSend(SUBSCRIPTION_URL + userId, map);
    }
}
