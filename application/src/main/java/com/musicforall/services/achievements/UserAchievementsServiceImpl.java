package com.musicforall.services.achievements;

import com.musicforall.common.Constants;
import com.musicforall.common.dao.Dao;
import com.musicforall.model.user.UserAchievement;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.musicforall.model.user.UserAchievement.INCREMENT_COUNT_QUERY;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.in;

/**
 * @author Evgeniy on 17.10.2016.
 */
@Service
@Transactional
public class UserAchievementsServiceImpl implements UserAchievementsService {
    @Autowired
    private Dao dao;

    @Override
    public UserAchievement save(UserAchievement achievement) {
        return dao.save(achievement);
    }

    @Override
    public UserAchievement get(Integer id) {
        return dao.get(UserAchievement.class, id);
    }

    @Override
    public UserAchievement incrementProgressCount(UserAchievement achievement) {
        final Map<String, Object> params = new HashMap<>();
        params.put(Constants.ID, achievement.getId());

        dao.update(INCREMENT_COUNT_QUERY, params);
        return get(achievement.getId());
    }

    @Override
    public Collection<UserAchievement> saveAll(Collection<UserAchievement> achievements) {
        return dao.saveAll(achievements);
    }

    @Override
    public Collection<UserAchievement> getByUserIdInStatuses(Integer userId, UserAchievement.Status... statuses) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(UserAchievement.class);
        criteria.add(eq("user.id", userId));
        if (statuses != null && statuses.length > 0) {
            criteria.add(in("status", statuses));
        }

        return dao.getAllBy(criteria);
    }

    @Override
    public Collection<UserAchievement> getAllByUserId(Integer userId) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(UserAchievement.class);
        criteria.add(eq("user.id", userId));
        return dao.getAllBy(criteria);
    }
}
