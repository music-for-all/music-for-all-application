package com.musicforall.services.achievements;

import com.musicforall.model.user.UserAchievement;

import java.util.Collection;

/**
 * @author Evgeniy on 17.10.2016.
 */
public interface UserAchievementsService {
    UserAchievement save(UserAchievement achievement);

    UserAchievement get(Integer id);

    UserAchievement incrementProgressCount(UserAchievement achievement);

    Collection<UserAchievement> saveAll(Collection<UserAchievement> achievements);

    Collection<UserAchievement> getByUserIdInStatuses(Integer userId, UserAchievement.Status... statuses);
}
