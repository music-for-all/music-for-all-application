package com.musicforall.services.artists;

import com.musicforall.model.Artist;
import com.musicforall.model.SearchArtistRequest;
import com.musicforall.services.artist.ArtistService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

/**
 * Created by Pavel Podgorniy on 8/19/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class ArtistsServiceTest {
    public static final String ARTIST_FOR_SAVE = "artist_for_save";

    @Autowired
    private ArtistService artistService;

    @Test
    public void testSaveArtistWithoutTags() {
        final Artist artist1 = new Artist("artist_for_save1");
        Artist savesArtist = artistService.save(artist1);
        assertEquals(artist1, savesArtist);
    }

    @Test
    public void testGetArtist() {
        final Artist artist1 = new Artist("artist_for_save1");
        artistService.save(artist1);
        final Artist savesArtists = artistService.get(artist1.getId());
        assertNotNull(savesArtists);
    }

    @Test
    public void testSaveAll() {
        final Set<Artist> artistSet = new HashSet<>(Arrays.asList(new Artist("artist1"), new Artist("artist2")));
        assertEquals(2, artistService.saveAll(artistSet).size());
    }

    @Test
    public void testGetAllLike() {
        final Set<Artist> artistSet = new HashSet<>(Arrays.asList(new Artist("testArtist1"), new Artist("testArtist2")));
        artistService.saveAll(artistSet);
        final SearchArtistRequest searchCriteria = new SearchArtistRequest("test", Arrays.asList());
        final List<Artist> queryArtists = artistService.getAllLike(searchCriteria);
        assertEquals(2, queryArtists.size());
    }

    @Test
    public void testDeleteTrack() {
        final Artist artist = artistService.save(new Artist("artist_for_delete"));
        artistService.delete(artist.getId());
        assertNull(artistService.get(artist.getId()));

    }
}
