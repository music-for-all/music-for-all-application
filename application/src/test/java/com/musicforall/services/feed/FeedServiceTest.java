package com.musicforall.services.feed;

import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.model.User;
import com.musicforall.services.follower.FollowerService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.musicforall.history.handlers.events.EventType.*;
import static junit.framework.Assert.assertTrue;

/**
 * @author IliaNik on 02.09.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        FeedTestExecutionListener.class})
@ActiveProfiles("dev")
public class FeedServiceTest {

    public static final String LOC_1 = "loc1";
    private static final String PASSWORD = "password";
    private static final int TRACK_ID = 3333;
    private static final int PLAYLIST_ID = 111;
    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FollowerService followerService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private PlaylistService playlistService;


    @Test
    public void testGetGroupedFollowingHistories() {
        final User user = new User("Adolf", PASSWORD, "meinkampf@example.com");
        final User user1 = new User("Iosiv", PASSWORD, "tribunal@example.com");
        final User user2 = new User("Winston", PASSWORD, "UK@example.com");

        final Track track1 = new Track("Kanye West - All day", "All day", "Kanye West", null, LOC_1, null);
        final Track track2 = new Track("Ray Charles – Mess around", "Mess around", "Ray Charles", null, LOC_1, null);
        final Playlist playlist3 = new Playlist("Jazz", null, user2);

        userService.save(user);
        userService.save(user1);
        userService.save(user2);
        trackService.save(track1);
        trackService.save(track2);
        playlistService.save(playlist3);


        final int TRACK_ID1 = track1.getId();
        final int TRACK_ID2 = track2.getId();
        final int PLAYLIST_ID3 = playlist3.getId();

        final int USER1_ID = user1.getId();
        final int USER2_ID = user2.getId();
        final int USER_ID = user.getId();

        followerService.follow(user.getId(), USER1_ID);
        followerService.follow(user.getId(), USER2_ID);
        History history1 = new History(TRACK_ID1, PLAYLIST_ID,
                new Date(), USER1_ID, TRACK_LISTENED);
        History history2 = new History(TRACK_ID2, PLAYLIST_ID,
                new Date(new Date().getTime() + 1), USER1_ID, TRACK_LIKED);
        History history3 = new History(TRACK_ID, PLAYLIST_ID3,
                new Date(new Date().getTime() + 2), USER2_ID, PLAYLIST_ADDED);

        historyService.record(history1);
        historyService.record(history2);
        historyService.record(history3);

        final Feed feed1 = new Feed(history1.getEventType(), "Kanye West - All day", history1.getDate());
        final Feed feed2 = new Feed(history2.getEventType(), "Ray Charles – Mess around", history2.getDate());
        final Feed feed3 = new Feed(history3.getEventType(), "Jazz", history3.getDate());

        Map<User, List<Feed>> followingHistories = feedService.getGroupedFollowingFeeds(USER_ID);

        assertTrue(followingHistories.get(user1).stream().anyMatch(f -> f.equals(feed1)));
        assertTrue(followingHistories.get(user1).stream().anyMatch(f -> f.equals(feed2)));
        assertTrue(followingHistories.get(user1).stream().anyMatch(f -> f.equals(feed3)));
    }
}