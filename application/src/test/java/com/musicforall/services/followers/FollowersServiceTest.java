package com.musicforall.services.followers;

import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static com.musicforall.history.handlers.events.EventType.TRACK_LIKED;
import static com.musicforall.history.handlers.events.EventType.TRACK_LISTENED;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by andrey on 8/2/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class FollowersServiceTest {

    private static final String PASSWORD = "password";
    private static final int TRACK_ID = 1111;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FollowerService followerService;

    @Test
    public void testFollow() {
        final User user1 = new User("Mike", PASSWORD, "mike@example.com");
        final User user2 = new User("John2", PASSWORD, "john2@example.com");
        userService.save(user1);
        userService.save(user2);
        followerService.follow(user1.getId(), user1.getId());
        assertEquals(0, followerService.getFollowingId(user1.getId()).size());

        followerService.follow(user1.getId(), user2.getId());
        assertNotNull(followerService.getFollowingId(user1.getId()));
    }

    @Test
    public void testUnfollow() {
        final User user = new User("John1", PASSWORD, "test@example.com");
        final User user_followers = new User("Spocks", PASSWORD, "mail1@example.com");
        userService.save(user);
        userService.save(user_followers);
        followerService.follow(user_followers.getId(), user.getId());
        followerService.unfollow(user_followers.getId(), user.getId());

        assertEquals(0, followerService.getFollowingId(user_followers.getId()).size());
    }

    @Test
    public void testGetFollowerId() {
        final User user = new User("Anna", PASSWORD, "anna@example.com");
        final User user_followers = new User("James", PASSWORD, "james@example.com");
        userService.save(user);
        userService.save(user_followers);

        followerService.follow(user_followers.getId(), user.getId());
        assertEquals(1, followerService.getFollowersId(user.getId()).size());

        followerService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, followerService.getFollowersId(user.getId()).size());
    }

    @Test
    public void testGetFollowingId() {
        final User user = new User("Tasha", PASSWORD, "tasha@example.com");
        final User user_followers = new User("Abrams", PASSWORD, "abrams@example.com");
        userService.save(user);
        userService.save(user_followers);
        followerService.follow(user_followers.getId(), user.getId());
        assertEquals(1, followerService.getFollowingId(user_followers.getId()).size());

        followerService.unfollow(user_followers.getId(), user.getId());
        assertEquals(0, followerService.getFollowingId(user_followers.getId()).size());
    }

    @Test
    public void testGetGroupedFollowingHistories() {
        final User user = new User("Adolf", PASSWORD, "meinkampf@example.com");
        final User user1 = new User("Iosiv", PASSWORD, "tribunal@example.com");
        final User user2 = new User("Winston", PASSWORD, "UK@example.com");

        userService.save(user);
        userService.save(user1);
        userService.save(user2);

        final int USER1_ID = user1.getId();
        final int USER2_ID = user2.getId();
        final int USER_ID = user.getId();

        followerService.follow(user.getId(), USER1_ID);
        followerService.follow(user.getId(), USER2_ID);
        History history1 = new History(TRACK_ID, new Date(), USER1_ID, TRACK_LISTENED);
        History history2 = new History(TRACK_ID, new Date(new Date().getTime() + 1), USER1_ID, TRACK_LIKED);
        History history3 = new History(TRACK_ID, new Date(new Date().getTime() + 2), USER2_ID, TRACK_LISTENED);

        historyService.record(history1);
        historyService.record(history2);
        historyService.record(history3);

        LinkedHashMap<User, List<History>> followingHistories = (LinkedHashMap<User, List<History>>) followerService
                .getGroupedFollowingHistories(USER_ID);

        assertTrue(followingHistories.get(user1).contains(history1));
        assertTrue(followingHistories.get(user1).contains(history2));
        assertTrue(followingHistories.get(user2).contains(history3));
    }
}
