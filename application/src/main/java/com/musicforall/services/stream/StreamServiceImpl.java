package com.musicforall.services.stream;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.model.Track;
import com.musicforall.model.user.User;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Evgeniy on 06.10.2016.
 */
@Service
public class StreamServiceImpl implements StreamService {

    @Autowired
    private CacheProvider<Integer, Track> cache;
    @Autowired
    private TrackService trackService;
    @Autowired
    private UserService userService;

    @Override
    public void start(Integer userId, Integer trackId) {
        final Track track = trackService.get(trackId);
        cache.put(userId, track);
    }

    @Override
    public void stop(Integer userId) {
        cache.remove(userId);
    }

    @Override
    public void publish(Integer userId, boolean toPublish) {
        final User currentUser = userService.getWithSettingsById(userId);
        currentUser.getSettings().setPublicRadio(toPublish);
        userService.save(currentUser);
    }

    @Override
    public Map<Integer, Track> getGroupedPublicStreams(Collection<Integer> ids) {
        final List<User> users = userService.getAllWithSettingsByIds(ids);
        return users.stream()
                .filter(u -> u.getSettings() != null && u.getSettings().isPublicRadio())
                .map(User::getId)
                .collect(HashMap::new, ((m, id) -> {
                    final Track track = cache.get(id);
                    if (track != null) {
                        m.put(id, track);
                    }
                }), HashMap::putAll);
    }
}
