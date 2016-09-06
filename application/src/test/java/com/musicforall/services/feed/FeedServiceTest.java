package com.musicforall.services.feed;

import com.musicforall.dto.feed.Feed;
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

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static com.musicforall.history.handlers.events.EventType.PLAYLIST_ADDED;
import static com.musicforall.history.handlers.events.EventType.TRACK_LIKED;
import static junit.framework.Assert.assertTrue;

/**
 * @author IliaNik on 02.09.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class})
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
    public void testGetGroupedFollowingFeeds() {
        final User user = new User("Adolf", PASSWORD, "meinkampf@example.com");
        final User user1 = new User("Iosiv", PASSWORD, "tribunal@example.com");
        final User user2 = new User("Winston", PASSWORD, "UK@example.com");

        final Track track = new Track("Ray Charles – Mess around", "Mess around", "Ray Charles", null, LOC_1, null);
        final Playlist playlist = new Playlist("Jazz", null, user2);

        userService.save(user);
        userService.save(user1);
        userService.save(user2);

        trackService.save(track);
        playlistService.save(playlist);

        final int USER1_ID = user1.getId();
        final int USER2_ID = user2.getId();

        followerService.follow(user.getId(), USER1_ID);
        followerService.follow(user.getId(), USER2_ID);

        final History history1 = new History(track.getId(), FeedServiceTest.PLAYLIST_ID,
                new Date(new Date().getTime() + 1), USER1_ID, TRACK_LIKED);

        final History history2 = new History(FeedServiceTest.TRACK_ID, playlist.getId(),
                new Date(new Date().getTime() + 2), USER2_ID, PLAYLIST_ADDED);

        historyService.record(history1);
        historyService.record(history2);

        final Feed feed1 = new Feed("<@spring.message \"followingpage.likedTrack\"/>" + " " +
                "Ray Charles – Mess around", history1.getDate());
        final Feed feed2 = new Feed("<@spring.message \"followingpage.likedTrack\"/>" + " " +
                "Jazz", history2.getDate());

        final Map<User, Collection<Feed>> followingHistories = feedService.getGroupedFollowingFeeds(user.getId());

        assertTrue(followingHistories.get(user1).stream().anyMatch(f -> f.equals(feed1)));
        assertTrue(followingHistories.get(user2).stream().anyMatch(f -> f.equals(feed2)));
    }
}