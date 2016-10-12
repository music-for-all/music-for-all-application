package com.musicforall.services.achievements;

import com.musicforall.model.Achievement;
import com.musicforall.model.user.UserAchievement;
import com.musicforall.util.ServicesTestConfig;
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
import static com.musicforall.model.user.UserAchievement.Status.IN_PROGRESS;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * @author ENikolskiy.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class AchievementsServiceTest {
    @Autowired
    private AchievementsService achievementsService;

    @Test
    public void saveAchievement() {
        final Achievement achievement = achievementsService.save(new Achievement("script", TRACK_ADDED));
        final Achievement savedAchievement = achievementsService.getAchievement(achievement.getId());

        assertEquals(achievement, savedAchievement);
    }

    @Test
    public void saveUserAchievement() {
        final Achievement achievement = new Achievement("script", TRACK_ADDED);
        final UserAchievement inProgress = achievementsService.save(new UserAchievement(achievement, IN_PROGRESS));
        final UserAchievement savedAchievement = achievementsService.getUserAchievement(inProgress.getId());

        assertEquals(inProgress, savedAchievement);
    }

    @Test
    public void incrementProgressCount() {
        final Achievement achievement = new Achievement("script", TRACK_ADDED);
        final UserAchievement inProgress = achievementsService.save(new UserAchievement(achievement, IN_PROGRESS));
        final UserAchievement savedAchievement = achievementsService.incrementProgressCount(inProgress);

        assertEquals(1, savedAchievement.getProgressCount() - inProgress.getProgressCount());
    }

    @Test
    public void getAllNotInIds() {
        final Achievement achievement1 = new Achievement("script1", TRACK_ADDED);
        final Achievement achievement2 = new Achievement("script2", TRACK_DELETED);
        final Achievement achievement3 = new Achievement("script3", TRACK_LIKED);
        achievementsService.saveAll(asList(achievement1, achievement2, achievement3));

        final List<Integer> excludedIds = asList(achievement1.getId(), achievement2.getId());
        final Collection<Achievement> achievements = achievementsService.getAllNotInIds(excludedIds);

        assertTrue(achievements.contains(achievement3));
        assertFalse(achievements.contains(achievement1));
        assertFalse(achievements.contains(achievement2));
    }

    @Test
    public void getAllByEventType() {
        final List<Achievement> trackAddedTypes = asList(
                new Achievement("script1", TRACK_ADDED),
                new Achievement("script1", TRACK_ADDED),
                new Achievement("script1", TRACK_ADDED)
        );
        final List<Achievement> trackDeletedTypes = asList(
                new Achievement("script2", TRACK_DELETED),
                new Achievement("script2", TRACK_DELETED),
                new Achievement("script2", TRACK_DELETED)
        );
        final List<Achievement> playlistDeletedTypes = asList(
                new Achievement("script3", PLAYLIST_DELETED),
                new Achievement("script3", PLAYLIST_DELETED),
                new Achievement("script3", PLAYLIST_DELETED)
        );
        achievementsService.saveAll(trackAddedTypes);
        achievementsService.saveAll(trackDeletedTypes);
        achievementsService.saveAll(playlistDeletedTypes);

        Collection<Achievement> byEventType = achievementsService.getAllByEventType(TRACK_ADDED);

        assertTrue(trackAddedTypes.stream().allMatch(byEventType::contains));
        assertTrue(byEventType.stream().allMatch(ac -> TRACK_ADDED == ac.getEventType()));

        byEventType = achievementsService.getAllByEventType(TRACK_DELETED);

        assertTrue(trackDeletedTypes.stream().allMatch(byEventType::contains));
        assertTrue(byEventType.stream().allMatch(ac -> TRACK_DELETED == ac.getEventType()));
    }
}
