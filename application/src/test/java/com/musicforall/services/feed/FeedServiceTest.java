package com.musicforall.services.feed;

import com.musicforall.dto.feed.Feed;
import com.musicforall.dto.userFeedsDTO.UserFeedsDTO;
import com.musicforall.history.model.History;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Artist;
import com.musicforall.model.Playlist;
import com.musicforall.model.Track;
import com.musicforall.model.user.User;
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
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.MessageSource;

import java.util.*;
import java.util.stream.Collectors;

import static com.musicforall.history.handlers.events.EventType.*;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.easymock.PowerMock.expectPrivate;
import static org.powermock.api.easymock.PowerMock.replay;

/**
 * @author IliaNik on 02.09.2016.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({FeedServiceImpl.class})
public class FeedServiceTest {

    private static final int TRACK_ID = 3333;
    private static final int PLAYLIST_ID = 111;
    private static final int USER1_ID = 2;
    private static final int USER_ID = 1;
    private static final String TEST_MESSAGE = "Test message ";
    private static final String TEST_TIME = "Test time ";
    private static final int NINE = 9;

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
    private Artist artist;

    @Mock
    private Playlist playlist;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetGroupedFollowingFeeds() throws Exception {
        FeedServiceImpl tested = PowerMock.createPartialMock(FeedServiceImpl.class, "formatDate");

        final History history1 = new History(TRACK_ID, PLAYLIST_ID,
                new Date(new Date().getTime() + 1), USER1_ID, TRACK_LIKED);
        final History history2 = new History(TRACK_ID, PLAYLIST_ID,
                new Date(new Date().getTime() + 2), USER1_ID, TRACK_LISTENED);
        final History history3 = new History(TRACK_ID, PLAYLIST_ID,
                new Date(new Date().getTime() + 3), USER1_ID, TRACK_ADDED);
        final History history4 = new History(TRACK_ID, PLAYLIST_ID,
                new Date(new Date().getTime() + 4), USER1_ID, TRACK_DELETED);
        final History history5 = new History(TRACK_ID, PLAYLIST_ID,
                new Date(new Date().getTime() + 5), USER1_ID, PLAYLIST_DELETED);
        final History history6 = new History(TRACK_ID, PLAYLIST_ID,
                new Date(new Date().getTime() + 6), USER1_ID, PLAYLIST_ADDED);

        List<History> histories = Arrays.asList(history1, history2, history3, history4, history5, history6);

        when(followerService.getFollowingId(USER_ID)).thenReturn(Arrays.asList(histories.size()));
        when(userService.getUsersById(any())).thenReturn(Arrays.asList(user));
        when(historyService.getUsersHistories(any())).thenReturn(histories);
        when(trackService.getAllByIds(any())).thenReturn(Arrays.asList(track));
        when(playlistService.getAllByIds(any())).thenReturn(Arrays.asList(playlist));
        when(track.getId()).thenReturn(TRACK_ID);
        when(playlist.getId()).thenReturn(PLAYLIST_ID);
        when(artist.getName()).thenReturn("Ray Charles");
        when(track.getName()).thenReturn("Mess around");
        when(playlist.getName()).thenReturn("Jazz");
        when(user.getId()).thenReturn(USER1_ID);
        when(messageSource.getMessage(any(), any(), any())).thenReturn(TEST_MESSAGE);
        expectPrivate(tested, "modifyData", any()).andReturn(TEST_TIME);
        replay(tested);

        final List<UserFeedsDTO> followingHistories = feedService.getGroupedFollowingFeeds(USER_ID);
        final Collection<Feed> feedsFromHistories = followingHistories.get(0).getFeeds();
        final List<Feed> feeds = histories
                .stream()
                .map((h) -> new Feed(TEST_MESSAGE, formatDateTestCopy(h.getDate())))
                .collect(Collectors.toList());

        assertTrue(feedsFromHistories.stream().allMatch(feeds::contains));
    }

    private String formatDateTestCopy(Date date) {
        final Calendar feedDate = new GregorianCalendar();
        feedDate.setTime(date);
        final Calendar today = new GregorianCalendar();

        final int hours = feedDate.get(Calendar.HOUR_OF_DAY);
        final int minutes = feedDate.get(Calendar.MINUTE);
        StringBuilder formattedDate = new StringBuilder();
        String day = today.after(feedDate) ? "Yesterday" : "Today";
        formattedDate.append(day).append(" ");
        formattedDate.append(hours > NINE ? Integer.toString(hours) : "0" + hours).append(":");
        formattedDate.append(minutes > NINE ? Integer.toString(minutes) : "0" + minutes);

        return formattedDate.toString();
    }
}