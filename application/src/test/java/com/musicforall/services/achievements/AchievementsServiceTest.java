package com.musicforall.services.achievements;

import com.musicforall.history.handlers.events.EventType;
import com.musicforall.model.Achievement;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

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
        final Achievement achievement = achievementsService.save(new Achievement("script", EventType.TRACK_ADDED));
        final Achievement savedAchievement = achievementsService.getAchievement(achievement.getId());

        assertEquals(achievement, savedAchievement);
    }
}
