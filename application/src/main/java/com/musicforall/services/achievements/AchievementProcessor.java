package com.musicforall.services.achievements;

import com.musicforall.common.script.Script;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.model.Achievement;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserAchievement;
import com.musicforall.services.AchievementsService;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.musicforall.model.user.UserAchievement.Status.DONE;
import static com.musicforall.model.user.UserAchievement.Status.IN_PROGRESS;
import static java.util.stream.Collectors.toList;

/**
 * @author Evgeniy on 17.10.2016.
 */
@Component
public class AchievementProcessor {
    @Autowired
    private AchievementsService achievementsService;
    @Autowired
    private UserAchievementsService userAchievementsService;
    @Autowired
    private UserService userService;

    @Async
    public void process(Track track, Integer userId, EventType type) {
        final Map<String, Object> vars = new HashMap<>(1);
        vars.put("track", track);
        process(vars, userId, type);
    }

    @Async
    public void process(Playlist playlist, Integer userId, EventType type) {
        final Map<String, Object> vars = new HashMap<>(1);
        vars.put("playlist", playlist);
        process(vars, userId, type);
    }

    private void process(Map<String, Object> vars, Integer userId, EventType type) {
        userAchievementsService.getByUserIdInStatuses(userId, IN_PROGRESS)
                .stream()
                .filter(ua -> ua.getAchievement().getEventType() == type)
                .filter(ua -> runScript(ua.getAchievement(), vars))
                .map(userAchievementsService::incrementProgressCount)
                .filter(ua -> ua.getProgressCount() >= ua.getAchievement().getCount())
                .forEach(ua -> {
                    ua.setStatus(DONE);
                    userAchievementsService.save(ua);
                });

        final User user = userService.get(userId);

        final List<Integer> excludedIds = userAchievementsService.getAllByUserId(userId)
                .stream()
                .map(UserAchievement::getAchievement)
                .map(Achievement::getId)
                .collect(toList());
        final List<UserAchievement> newUA = achievementsService.filterBy(excludedIds, type).stream()
                .filter(a -> runScript(a, vars))
                .map(a -> new UserAchievement(user, a, a.getCount() > 1 ? IN_PROGRESS : DONE))
                .collect(toList());
        userAchievementsService.saveAll(newUA);
    }

    private boolean runScript(Achievement achievement, Map<String, Object> vars) {
        return Script.<Boolean>create(achievement.getScript(), vars).run();
    }
}
