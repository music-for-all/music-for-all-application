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
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.musicforall.model.Achievement.ALL_ACHIEVEMENTS_NOT_IN_IDS_QUERY;
import static com.musicforall.model.user.UserAchievement.INCREMENT_COUNT_QUERY;
import static com.musicforall.model.user.UserAchievement.Status.DONE;
import static com.musicforall.model.user.UserAchievement.Status.IN_PROGRESS;
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
        final Map<String, Object> params = new HashMap<>();
        params.put("ids", excludedIds);

        return dao.getAllByNamedQuery(Achievement.class, ALL_ACHIEVEMENTS_NOT_IN_IDS_QUERY, params);
    }

    @Override
    public Collection<Achievement> saveAll(Collection<Achievement> achievements) {
        return dao.saveAll(achievements);
    }

    @Override
    public Collection<Achievement> getAllByEventType(EventType type) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(Achievement.class);
        criteria.add(Property.forName("eventType").eq(type));

        return dao.getAllBy(criteria);
    }

    public Collection<Achievement> filterBy(EventType type, Collection<Integer> excludedIds) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(Achievement.class);
        criteria.add(Property.forName("eventType").eq(type));
        criteria.add(not(in("id", excludedIds)));

        return dao.getAllBy(criteria);
    }

    @Async
    private void doStuff(Map<String, Object> vars, Integer userId, EventType type) {
        final User user = userService.getUserWithAchievements(userId);
        final List<UserAchievement> userAchieve = user.getUserAchievements().stream()
                .filter(a -> a.getStatus() != DONE && a.getAchievement().getEventType() == type)
                .collect(toList());

        final Collection<UserAchievement> processed = userAchieve.stream()
                .filter(ua -> processScript(ua.getAchievement(), vars))
                .collect(toList());

        if (!processed.isEmpty()) {
            processed.stream()
                    .map(this::incrementProgressCount)
                    .forEach(ua -> {
                        if (ua.getProgressCount() == ua.getAchievement().getCount()) {
                            ua.setStatus(DONE);
                            save(ua);
                        }
                    });
        } else {
            final List<Integer> excludedIds = processed.stream().map(UserAchievement::getId).collect(toList());
            final List<UserAchievement> newUA = filterBy(type, excludedIds).stream()
                    .filter(a -> processScript(a, vars))
                    .map(a -> new UserAchievement(a, IN_PROGRESS))
                    .collect(toList());
            user.addUserAchievements(newUA);
            userService.save(user);
        }
    }

    private boolean processScript(Achievement achievement, Map<String, Object> vars) {
        return Script.<Boolean>create(achievement.getScript(), vars).run();
    }
}
