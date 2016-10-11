package com.musicforall.services.achievements;

import com.musicforall.common.Constants;
import com.musicforall.common.dao.Dao;
import com.musicforall.model.Achievement;
import com.musicforall.model.InProgressAchievement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.musicforall.model.InProgressAchievement.INCREMENT_COUNT_QUERY;

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

    @Override
    public InProgressAchievement incrementProgressCount(InProgressAchievement achievement) {
        final Map<String, Object> params = new HashMap<>();
        params.put(Constants.ID, achievement.getId());
        dao.update(INCREMENT_COUNT_QUERY, params);
        return getInProgressAchievement(achievement.getId());
    }
}
