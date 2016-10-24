package com.musicforall.services.stream;

import com.musicforall.common.cache.CacheProvider;
import com.musicforall.model.Track;
import com.musicforall.model.user.UserData;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.musicforall.config.CacheConfig.STREAM;

/**
 * @author Evgeniy on 06.10.2016.
 */
@Service
public class StreamServiceImpl implements StreamService {

    @Autowired
    @Qualifier(STREAM)
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
    public void switchRadio(Integer userId) {
        userService.switchPublicRadio(userId);
    }

    @Override
    public Map<Integer, Track> getGroupedPublicStreams(Collection<Integer> ids) {
        final List<UserData> users = userService.getAllUserDataByUserId(ids);
        return users.stream()
                .filter(UserData::isPublicRadio)
                .map(UserData::getUserId)
                .collect(HashMap::new, ((m, id) -> {
                    final Track track = cache.get(id);
                    if (track != null) {
                        m.put(id, track);
                    }
                }), HashMap::putAll);
    }
}
