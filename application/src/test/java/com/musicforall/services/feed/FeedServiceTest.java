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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static com.musicforall.history.handlers.events.EventType.PLAYLIST_ADDED;
import static com.musicforall.history.handlers.events.EventType.TRACK_LIKED;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author IliaNik on 02.09.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class FeedServiceTest {

    private static final String LOC_1 = "loc1";
    private static final int TRACK_ID = 3333;
    private static final int PLAYLIST_ID = 111;
    private static final int USER1_ID = 2;
    private static final int USER_ID = 1;
    private static final String testMessage = "Test message";

    @Mock
    private UserService userService;

    @Mock
    private HistoryService historyService;

    @Mock
    private FollowerService followerService;

    @InjectMocks
    private FeedServiceImpl feedService;

    @Mock
    private TrackService trackService;

    @Mock
    private PlaylistService playlistService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private User user;

    @Mock
    private Track track;

    @Mock
    private Playlist playlist;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetGroupedFollowingFeeds() {
/* final User user = new User("Adolf", PASSWORD, "meinkampf@example.com"); 
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

when(messageSource.getMessage(any(), any(), any())).thenReturn(testMessage); 

final Feed feed1 = new Feed(testMessage + " " + 
"Ray Charles – Mess around", history1.getDate()); 
final Feed feed2 = new Feed(testMessage + " " + 
"Jazz", history2.getDate());*/

        final History history1 = new History(TRACK_ID, PLAYLIST_ID,
                new Date(new Date().getTime() + 1), USER1_ID, TRACK_LIKED);

        final History history2 = new History(TRACK_ID, PLAYLIST_ID,
                new Date(new Date().getTime() + 2), USER1_ID, PLAYLIST_ADDED);

        when(followerService.getFollowingId(USER_ID)).thenReturn(Arrays.asList(2));
        when(userService.getUsersById(any())).thenReturn(Arrays.asList(user));
        when(historyService.getUsersHistories(any())).thenReturn(Arrays.asList(history1, history2));

        when(trackService.getAllByIds(any())).thenReturn(Arrays.asList(track));
        when(playlistService.getAllByIds(any())).thenReturn(Arrays.asList(playlist));
        when(track.getId()).thenReturn(TRACK_ID);
        when(playlist.getId()).thenReturn(PLAYLIST_ID);
        when(track.getEntireName()).thenReturn("Ray Charles – Mess around");
        when(playlist.getName()).thenReturn("Jazz");
        when(user.getId()).thenReturn(USER1_ID);
        when(messageSource.getMessage(any(), any(), any())).thenReturn(testMessage);

        final Map<User, Collection<Feed>> followingHistories = feedService.getGroupedFollowingFeeds(USER_ID);

        final Feed feed1 = new Feed(testMessage + " " +
                "Ray Charles – Mess around", history1.getDate());
        final Feed feed2 = new Feed(testMessage + " " +
                "Jazz", history2.getDate());

        assertTrue(followingHistories.get(user).stream().anyMatch(f -> f.equals(feed1)));
        assertTrue(followingHistories.get(user).stream().anyMatch(f -> f.equals(feed2)));

    }
}