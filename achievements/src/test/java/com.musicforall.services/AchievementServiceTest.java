package com.musicforall.services;

import com.musicforall.config.ServicesTestConfig;
import com.musicforall.model.Achievement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Collection;
import java.util.List;

import static com.musicforall.history.handlers.events.EventType.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

/**
 * @author Evgeniy on 17.10.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class AchievementServiceTest {
    @Autowired
    private AchievementsService achievementsService;

    @Test
    public void saveAchievement() {
        final Achievement achievement = achievementsService.save(new Achievement("saveAchievement", TRACK_ADDED, 0));
        final Achievement savedAchievement = achievementsService.get(achievement.getId());

        assertEquals(achievement, savedAchievement);
    }

    @Test
    public void getAllNotInIds() {
        final Achievement achievement1 = new Achievement("getAllNotInIds1", TRACK_ADDED, 0);
        final Achievement achievement2 = new Achievement("getAllNotInIds2", TRACK_DELETED, 0);
        final Achievement achievement3 = new Achievement("getAllNotInIds3", TRACK_LIKED, 0);
        achievementsService.saveAll(asList(achievement1, achievement2, achievement3));

        final List<Integer> excludedIds = asList(achievement1.getId(), achievement2.getId());
        final Collection<Achievement> achievements = achievementsService.getAllNotInIds(excludedIds);

        assertTrue(achievements.contains(achievement3));
        assertFalse(achievements.contains(achievement1));
        assertFalse(achievements.contains(achievement2));
    }

    @Test
    public void filterBy() {
        final List<Achievement> trackAddedTypes = asList(
                new Achievement("filterBy11", TRACK_ADDED, 0),
                new Achievement("filterBy12", TRACK_ADDED, 0),
                new Achievement("filterBy13", TRACK_ADDED, 0)
        );
        final List<Achievement> trackDeletedTypes = asList(
                new Achievement("filterBy21", TRACK_DELETED, 0),
                new Achievement("filterBy22", TRACK_DELETED, 0),
                new Achievement("filterBy23", TRACK_DELETED, 0)
        );
        final List<Achievement> playlistDeletedTypes = asList(
                new Achievement("filterBy31", PLAYLIST_DELETED, 0),
                new Achievement("filterBy32", PLAYLIST_DELETED, 0),
                new Achievement("filterBy33", PLAYLIST_DELETED, 0)
        );
        achievementsService.saveAll(trackAddedTypes);
        achievementsService.saveAll(trackDeletedTypes);
        achievementsService.saveAll(playlistDeletedTypes);

        Collection<Achievement> filtered = achievementsService.filterBy(emptyList(), TRACK_ADDED);
        assertTrue(filtered.stream().allMatch(a -> a.getEventType() == TRACK_ADDED));

        final List<Integer> excludedIds = asList(1, 2, 3, 4, 5);

        filtered = achievementsService.filterBy(excludedIds, TRACK_ADDED, TRACK_DELETED);
        assertTrue(filtered.stream().allMatch(a -> a.getEventType().in(TRACK_ADDED, TRACK_DELETED)));
        assertFalse(filtered.stream().anyMatch(a -> excludedIds.contains(a.getId())));

        filtered = achievementsService.filterBy(null);
        assertTrue(filtered.containsAll(trackAddedTypes));
        assertTrue(filtered.containsAll(trackDeletedTypes));
    }
}
