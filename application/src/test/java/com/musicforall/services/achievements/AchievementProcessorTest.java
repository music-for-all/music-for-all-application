package com.musicforall.services.achievements;

import com.musicforall.model.Achievement;
import com.musicforall.model.Playlist;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import com.musicforall.model.user.User;
import com.musicforall.model.user.UserAchievement;
import com.musicforall.services.AchievementsService;
import com.musicforall.services.playlist.PlaylistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Collection;

import static com.google.common.collect.Sets.newHashSet;
import static com.musicforall.common.Constants.NAME;
import static com.musicforall.common.Constants.PASSWORD;
import static com.musicforall.history.handlers.events.EventType.*;
import static com.musicforall.model.user.UserAchievement.Status.DONE;
import static com.musicforall.model.user.UserAchievement.Status.IN_PROGRESS;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class AchievementProcessorTest {
    @Autowired
    private AchievementProcessor processor;
    @Autowired
    private UserAchievementsService userAchievementsService;
    @Autowired
    private AchievementsService achievementsService;
    @Autowired
    private UserService userService;
    @Autowired
    private TrackService trackService;
    @Autowired
    private PlaylistService playlistService;

    @Test
    public void testProcessTrack() throws Exception {
        final User user = userService.save(new User(NAME, PASSWORD, "processTrack@sabaka.com"));

        achievementsService.saveAll(asList(
                new Achievement("track.name.equals('name1')", TRACK_ADDED, 5),
                new Achievement("track.name.equals('name2')", TRACK_DELETED, 4),
                new Achievement("track.name.equals('name3')", TRACK_LIKED, 3),
                new Achievement("track.name.equals('name4')", TRACK_LISTENED, 2),
                new Achievement("track.name.equals('name5')", TRACK_LISTENED, 1)
        ));

        userAchievementsService.saveAll(asList(
                new UserAchievement(user, new Achievement("track.tags.size() > 0", TRACK_ADDED, 2), IN_PROGRESS)
        ));

        final Track track = trackService.save(new Track("name1", "location1", newHashSet(
                new Tag("processTrackTag1"), new Tag("processTrackTag2")
        )));

        processor.process(track, user.getId(), TRACK_ADDED);

        final Collection<UserAchievement> userAchievements =
                userAchievementsService.getByUserIdInStatuses(user.getId());

        assertEquals(2, userAchievements.size());
        assertTrue(userAchievements.stream().anyMatch(ua -> ua.getStatus() == DONE));
        assertTrue(userAchievements.stream().anyMatch(ua -> ua.getStatus() == IN_PROGRESS));
    }

    @Test
    public void testProcessPlaylist() throws Exception {
        final User user = userService.save(new User(NAME, PASSWORD, "processPlaylist@sabaka.com"));

        achievementsService.saveAll(asList(
                new Achievement("playlist.name.equals('name1')", PLAYLIST_ADDED, 5),
                new Achievement("playlist.name.equals('name2')", PLAYLIST_DELETED, 4),
                new Achievement("playlist.name.equals('name3')", PLAYLIST_ADDED, 3),
                new Achievement("playlist.name.equals('name4')", PLAYLIST_DELETED, 2),
                new Achievement("playlist.name.equals('name5')", PLAYLIST_ADDED, 1)
        ));

        userAchievementsService.saveAll(asList(
                new UserAchievement(user, new Achievement("playlist.tracks.size() > 0", PLAYLIST_ADDED, 2), IN_PROGRESS),
                new UserAchievement(user, new Achievement("playlist.tracks.size() > 100", PLAYLIST_DELETED, 25), IN_PROGRESS),
                new UserAchievement(user, new Achievement("playlist.tracks.size() > 0", PLAYLIST_ADDED, 25), IN_PROGRESS)
        ));

        final Playlist playlist = playlistService.save(new Playlist("name1", newHashSet(
                new Track("processPlaylistTrack1", "loc1"), new Track("processPlaylistTrack2", "loc2")), user));

        processor.process(playlist, user.getId(), PLAYLIST_ADDED);

        assertEquals(4, userAchievementsService.getByUserIdInStatuses(user.getId()).size());
        assertEquals(1, userAchievementsService.getByUserIdInStatuses(user.getId(), DONE).size());
        assertEquals(3, userAchievementsService.getByUserIdInStatuses(user.getId(), IN_PROGRESS).size());
    }
}