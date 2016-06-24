package com.musicforall;

import com.musicforall.model.Song;
import com.musicforall.model.Tag;
import com.musicforall.services.song.SongService;
import com.musicforall.services.tag.TagService;
import com.musicforall.util.JpaServicesTestConfig;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pukho on 22.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {JpaServicesTestConfig.class,
        ServicesTestConfig.class})
public class TagServiceTest {

    @Autowired
    public SongService songService;

    @Autowired
    private TagService tagService;

    @Test
    public void testAddTag(){
        Set<Tag> tags = new HashSet<Tag>();
        tags.add(new Tag("rock")); tags.add(new Tag("alternative"));

        Song song = new Song("song2", "path2");
        songService.save(song);
        tagService.addTag(song.getId(), tags);

        tagService.addTag(song.getId(), tags);

        assertTrue(tagService.getAllTags().size()==2);
        assertNotNull(tagService.isTagExist("rock"));
        assertNull(tagService.isTagExist("rock_is_dead"));
    }

    @Test
    public void testTag(){
        Set<Tag> tags = new HashSet<>(); tags.add(new Tag("rock"));
        Song song = new Song(tags, "name", "path");
        songService.save(song);

        Song song2 = new Song(tags, "name", "path");
        songService.save(song2);
    }
}
