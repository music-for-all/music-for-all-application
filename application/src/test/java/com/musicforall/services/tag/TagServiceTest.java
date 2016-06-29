package com.musicforall.services.tag;

import com.musicforall.model.Tag;
import com.musicforall.services.track.TrackService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pukho on 22.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
public class TagServiceTest {

    @Autowired
    public TrackService trackService;

    @Autowired
    private TagService tagService;

    @Test
    public void testSaveTag_IsTagExist() {
        final Tag tag = tagService.save("tag_for_save");

        assertTrue(tagService.isTagExist("tag_for_save"));
        assertFalse(tagService.isTagExist("tag_is_not_exist"));
    }

    @Test
    public void testSaveAllTags_GetAllTags() {
        Tag tag1 = new Tag("tag_for_save1");
        Tag tag2 = new Tag("tag_for_save2");

        final Collection<Tag> savesTags = tagService.saveAll(Arrays.asList(tag1, tag2));
        assertTrue(tagService.getAllTags().containsAll(savesTags));
    }

    @Test
    public void testGetTracks() {
        final Tag tag = tagService.save("tag_for_test_get");
        final Tag expectedTag = tagService.get("tag_for_test_get");

        assertEquals(tag, expectedTag);
    }

    @Test
    public void GetTagIsNotExist() {
        assertNull(tagService.get("this_tag_isnt_exist"));
    }
}
