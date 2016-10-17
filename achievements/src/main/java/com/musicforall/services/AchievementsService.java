package com.musicforall.services;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.model.Achievement;

import java.util.Collection;

/**
 * @author ENikolskiy.
 */
public interface AchievementsService {

    Achievement save(Achievement achievement);

    Achievement get(Integer id);

    Collection<Achievement> getAllNotInIds(Collection<Integer> excludedIds);

    Collection<Achievement> saveAll(Collection<Achievement> achievements);

    Collection<Achievement> filterBy(Collection<Integer> excludedIds, EventType... types);
}
