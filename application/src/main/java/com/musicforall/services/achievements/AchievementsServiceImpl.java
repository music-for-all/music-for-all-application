package com.musicforall.services.achievements;

import com.musicforall.common.Constants;
import com.musicforall.common.dao.Dao;
import com.musicforall.common.script.Script;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.model.Achievement;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserAchievement;
import com.musicforall.services.user.UserService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.musicforall.model.user.UserAchievement.INCREMENT_COUNT_QUERY;
import static com.musicforall.model.user.UserAchievement.Status.DONE;
import static com.musicforall.model.user.UserAchievement.Status.IN_PROGRESS;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.not;

/**
 * @author ENikolskiy.
 */
@Service
@Transactional
public class AchievementsServiceImpl implements AchievementsService {
    @Autowired
    private Dao dao;
    @Autowired
    private UserService userService;

    @Override
    public Achievement save(Achievement achievement) {
        return dao.save(achievement);
    }

    @Override
    public UserAchievement save(UserAchievement achievement) {
        return dao.save(achievement);
    }

    @Override
    public Achievement getAchievement(Integer id) {
        return dao.get(Achievement.class, id);
    }

    @Override
    public UserAchievement getUserAchievement(Integer id) {
        return dao.get(UserAchievement.class, id);
    }

    @Override
    public UserAchievement incrementProgressCount(UserAchievement achievement) {
        final Map<String, Object> params = new HashMap<>();
        params.put(Constants.ID, achievement.getId());

        dao.update(INCREMENT_COUNT_QUERY, params);
        return getUserAchievement(achievement.getId());
    }

    @Override
    public Collection<Achievement> getAllNotInIds(Collection<Integer> excludedIds) {
        return filterBy(excludedIds, asList(EventType.values()));
    }

    @Override
    public Collection<Achievement> saveAll(Collection<Achievement> achievements) {
        return dao.saveAll(achievements);
    }

    @Override
    public Collection<Achievement> filterBy(Collection<Integer> excludedIds, Collection<EventType> types) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(Achievement.class);
        criteria.add(in("eventType", types));
        criteria.add(not(in("id", excludedIds)));

        return dao.getAllBy(criteria);
    }

    private void doStuff(Map<String, Object> vars, Integer userId, EventType type) {
        final User user = userService.getUserWithAchievements(userId);

        final List<UserAchievement> processed = user.getUserAchievements().stream()
                .filter(ua -> ua.getStatus() != DONE && ua.getAchievement().getEventType() == type)
                .filter(ua -> processScript(ua.getAchievement(), vars))
                .collect(toList());

        if (!processed.isEmpty()) {
            processed.stream()
                    .map(this::incrementProgressCount)
                    .filter(ua -> ua.getProgressCount() == ua.getAchievement().getCount())
                    .forEach(ua -> {
                        ua.setStatus(DONE);
                        save(ua);
                    });
        }
        final List<Integer> excludedIds = user.getUserAchievements().stream()
                .map(UserAchievement::getId)
                .collect(toList());
        final List<UserAchievement> newUA = filterBy(excludedIds, asList(type)).stream()
                .filter(a -> processScript(a, vars))
                .map(a -> new UserAchievement(a, IN_PROGRESS))
                .collect(toList());
        if (!newUA.isEmpty()) {
            user.addUserAchievements(newUA);
            userService.save(user);
        }
    }

    private boolean processScript(Achievement achievement, Map<String, Object> vars) {
        return Script.<Boolean>create(achievement.getScript(), vars).run();
    }
}
