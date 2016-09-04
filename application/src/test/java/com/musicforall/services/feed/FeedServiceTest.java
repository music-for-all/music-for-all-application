package com.musicforall.services.feed;

import com.musicforall.dto.feed.Feed;
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
import java.util.List;
import java.util.Map;

import static com.musicforall.history.handlers.events.EventType.TRACK_LIKED;
import static com.musicforall.history.handlers.events.EventType.TRACK_LISTENED;
import static junit.framework.Assert.assertTrue;

/**
 * @author IliaNik on 02.09.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class FeedServiceTest {

    private static final String PASSWORD = "password";
    private static final int TRACK_ID = 1111;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private FeedService feedService;


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
        History history1 = new History(TRACK_ID, null, new Date(), USER1_ID, TRACK_LISTENED);
        History history2 = new History(TRACK_ID, null, new Date(new Date().getTime() + 1), USER1_ID, TRACK_LIKED);
        History history3 = new History(TRACK_ID, null, new Date(new Date().getTime() + 2), USER2_ID, TRACK_LISTENED);

        historyService.record(history1);
        historyService.record(history2);
        historyService.record(history3);

        Map<User, List<Feed>> followingHistories = feedService.getGroupedFollowingFeeds(USER_ID);

        assertTrue(followingHistories.get(user1).contains(history1));
        assertTrue(followingHistories.get(user1).contains(history2));
        assertTrue(followingHistories.get(user2).contains(history3));
    }
}