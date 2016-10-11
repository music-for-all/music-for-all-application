package com.musicforall.services.achievements;

import com.musicforall.model.Achievement;
import com.musicforall.model.InProgressAchievement;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.musicforall.history.handlers.events.EventType.TRACK_ADDED;
import static org.junit.Assert.assertEquals;

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
    public void saveInProgressAchievement() {
        final Achievement achievement = new Achievement("script", TRACK_ADDED);
        final InProgressAchievement inProgress = achievementsService.save(new InProgressAchievement(achievement));
        final InProgressAchievement savedAchievement = achievementsService.getInProgressAchievement(inProgress.getId());

        assertEquals(inProgress, savedAchievement);
    }

    @Test
    public void incrementProgressCount() {
        final Achievement achievement = new Achievement("script", TRACK_ADDED);
        final InProgressAchievement inProgress = achievementsService.save(new InProgressAchievement(achievement));
        achievementsService.incrementProgressCount(inProgress);
        final InProgressAchievement savedAchievement = achievementsService.getInProgressAchievement(inProgress.getId());

        assertEquals(1, savedAchievement.getCount() - inProgress.getCount());
    }
}
