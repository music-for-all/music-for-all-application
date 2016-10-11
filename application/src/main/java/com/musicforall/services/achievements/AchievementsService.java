package com.musicforall.services.achievements;

import com.musicforall.model.Achievement;
import com.musicforall.model.InProgressAchievement;

/**
 * @author ENikolskiy.
 */
public interface AchievementsService {

    Achievement save(Achievement achievement);

    InProgressAchievement save(InProgressAchievement achievement);

    Achievement getAchievement(Integer id);

    InProgressAchievement getInProgressAchievement(Integer id);

    InProgressAchievement incrementProgressCount(InProgressAchievement achievement);
}
