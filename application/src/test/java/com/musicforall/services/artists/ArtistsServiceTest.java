package com.musicforall.services.artists;

import com.musicforall.model.Artist;
import com.musicforall.model.Tag;
import com.musicforall.services.artist.ArtistService;
import com.musicforall.services.tag.TagService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

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
    public void testSaveAllArtistsGetAllArtists() {
        final Artist artist1 = new Artist("artist_for_save1");

        final Artist savesArtists = artistService.save(artist1);
        assertTrue(artistService.getAllArtists().contains(savesArtists));
    }
}
