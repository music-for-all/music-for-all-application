package com.musicforall.services;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.model.Achievement;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

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

    @Override
    public Achievement save(Achievement achievement) {
        return dao.save(achievement);
    }

    @Override
    public Achievement get(Integer id) {
        return dao.get(Achievement.class, id);
    }

    @Override
    public Collection<Achievement> getAllNotInIds(Collection<Integer> excludedIds) {
        return filterBy(excludedIds, EventType.values());
    }

    @Override
    public Collection<Achievement> saveAll(Collection<Achievement> achievements) {
        return dao.saveAll(achievements);
    }

    @Override
    public Collection<Achievement> filterBy(Collection<Integer> excludedIds, EventType... types) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(Achievement.class);
        if (excludedIds != null && !excludedIds.isEmpty()) {
            criteria.add(not(in("id", excludedIds)));
        }
        if (types != null && types.length > 0) {
            criteria.add(in("eventType", types));
        }

        return dao.getAllBy(criteria);
    }
}
