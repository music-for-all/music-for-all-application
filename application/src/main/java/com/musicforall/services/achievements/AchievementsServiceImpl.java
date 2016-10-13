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
import static org.hibernate.criterion.Restrictions.*;

/**
 * @author ENikolskiy.
 */
@Service
@Transactional
//TODO split in two services
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
    public Collection<UserAchievement> saveUserAchievements(Collection<UserAchievement> achievements) {
        return dao.saveAll(achievements);
    }

    @Override
    public Collection<Achievement> filterBy(Collection<Integer> excludedIds, Collection<EventType> types) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(Achievement.class);
        criteria.add(in("eventType", types));
        criteria.add(not(in("id", excludedIds)));

        return dao.getAllBy(criteria);
    }

    @Override
    public Collection<UserAchievement> getByUserInStatuses(Integer userId, UserAchievement.Status... statuses) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(UserAchievement.class);
        criteria.add(eq("user.id", userId));
        if (statuses != null && statuses.length != 0) {
            criteria.add(in("status", statuses));
        }

        return dao.getAllBy(criteria);
    }

    //TODO place to some kind of AchievementProcessor
    private void doStuff(Map<String, Object> vars, Integer userId, EventType type) {

        final Collection<UserAchievement> userAchievements = getByUserInStatuses(userId, IN_PROGRESS);

        final List<UserAchievement> processed = userAchievements.stream()
                .filter(ua -> ua.getAchievement().getEventType() == type)
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
        final User user = userService.get(userId);

        final List<Integer> excludedIds = userAchievements.stream()
                .map(UserAchievement::getId)
                .collect(toList());
        final List<UserAchievement> newUA = filterBy(excludedIds, asList(type)).stream()
                .filter(a -> processScript(a, vars))
                .map(a -> new UserAchievement(user, a, IN_PROGRESS))
                .collect(toList());
        saveUserAchievements(newUA);
    }

    private boolean processScript(Achievement achievement, Map<String, Object> vars) {
        return Script.<Boolean>create(achievement.getScript(), vars).run();
    }
}
