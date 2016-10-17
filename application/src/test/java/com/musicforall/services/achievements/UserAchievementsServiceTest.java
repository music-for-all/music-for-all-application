package com.musicforall.services.achievements;

import com.musicforall.model.Achievement;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserAchievement;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.musicforall.common.Constants.*;
import static com.musicforall.history.handlers.events.EventType.TRACK_ADDED;
import static com.musicforall.model.user.UserAchievement.Status.DONE;
import static com.musicforall.model.user.UserAchievement.Status.IN_PROGRESS;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author ENikolskiy.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class UserAchievementsServiceTest {
    @Autowired
    private UserAchievementsService userAchievementsService;
    @Autowired
    private UserService userService;

    @Test
    public void saveUserAchievement() {
        final User user = userService.save(new User(USER, PASSWORD, "saveUserAchievement@mail.com"));
        final Achievement achievement = new Achievement("saveUserAchievement", TRACK_ADDED, 0);
        final UserAchievement inProgress = userAchievementsService.save(new UserAchievement(user, achievement, IN_PROGRESS));
        final UserAchievement savedAchievement = userAchievementsService.get(inProgress.getId());

        assertEquals(inProgress, savedAchievement);
    }

    @Test
    public void incrementProgressCount() {
        final User user = userService.save(new User(USER, PASSWORD, "incrementProgressCount@mail.com"));
        final Achievement achievement = new Achievement("incrementProgressCount", TRACK_ADDED, 0);
        final UserAchievement inProgress = userAchievementsService.save(new UserAchievement(user, achievement, IN_PROGRESS));
        final UserAchievement savedAchievement = userAchievementsService.incrementProgressCount(inProgress);

        assertEquals(1, savedAchievement.getProgressCount() - inProgress.getProgressCount());
    }

    @Test
    public void getByUser() {
        final User user1 = userService.save(new User(NAME, PASSWORD, "getByUser1@mail.com"));
        final User user2 = userService.save(new User(NAME, PASSWORD, "getByUser2@mail.com"));

        userAchievementsService.saveAll(asList(
                new UserAchievement(user1, new Achievement("getByUser1", TRACK_ADDED, 3), IN_PROGRESS),
                new UserAchievement(user1, new Achievement("getByUser2", TRACK_ADDED, 5), DONE),
                new UserAchievement(user1, new Achievement("getByUser3", TRACK_ADDED, 3), IN_PROGRESS),

                new UserAchievement(user2, new Achievement("getByUser4", TRACK_ADDED, 3), IN_PROGRESS),
                new UserAchievement(user2, new Achievement("getByUser5", TRACK_ADDED, 7), DONE)
        ));
        assertEquals(3, userAchievementsService.getByUserIdInStatuses(user1.getId()).size());
        assertEquals(3, userAchievementsService.getByUserIdInStatuses(user1.getId(), null).size());
        assertEquals(2, userAchievementsService.getByUserIdInStatuses(user1.getId(), IN_PROGRESS).size());
        assertEquals(1, userAchievementsService.getByUserIdInStatuses(user1.getId(), DONE).size());

        assertEquals(2, userAchievementsService.getByUserIdInStatuses(user2.getId()).size());
        assertEquals(1, userAchievementsService.getByUserIdInStatuses(user2.getId(), IN_PROGRESS).size());
        assertEquals(1, userAchievementsService.getByUserIdInStatuses(user2.getId(), DONE).size());
    }
}
