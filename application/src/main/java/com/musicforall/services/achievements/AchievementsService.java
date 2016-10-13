package com.musicforall.services.achievements;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.model.Achievement;
import com.musicforall.model.user.UserAchievement;

import java.util.Collection;

/**
 * @author ENikolskiy.
 */
public interface AchievementsService {

    Achievement save(Achievement achievement);

    UserAchievement save(UserAchievement achievement);

    Achievement getAchievement(Integer id);

    UserAchievement getUserAchievement(Integer id);

    UserAchievement incrementProgressCount(UserAchievement achievement);

    Collection<Achievement> getAllNotInIds(Collection<Integer> excludedIds);

    Collection<Achievement> saveAll(Collection<Achievement> achievements);

    Collection<UserAchievement> saveUserAchievements(Collection<UserAchievement> achievements);

    Collection<Achievement> filterBy(Collection<Integer> excludedIds, Collection<EventType> types);

    Collection<UserAchievement> getByUserInStatuses(Integer userId, UserAchievement.Status... statuses);
}
