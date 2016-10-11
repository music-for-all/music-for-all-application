package com.musicforall.services.achievements;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Achievement;
import com.musicforall.model.InProgressAchievement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ENikolskiy.
 */
@Service
@Transactional
public class AchievementsServiceImpl implements AchievementsService {
    @Autowired
    private Dao dao;

    @Override
    public Achievement save(Achievement achievement) {
        return dao.save(achievement);
    }

    @Override
    public InProgressAchievement save(InProgressAchievement achievement) {
        return dao.save(achievement);
    }

    @Override
    public Achievement getAchievement(Integer id) {
        return dao.get(Achievement.class, id);
    }

    @Override
    public InProgressAchievement getInProgressAchievement(Integer id) {
        return dao.get(InProgressAchievement.class, id);
    }
}
