package com.musicforall.services.tag;

import com.musicforall.model.Tag;
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

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pukho on 22.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@ActiveProfiles("dev")
public class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Test
    public void testSaveAllTagsGetAllTags() {
        final Tag tag1 = new Tag("tag_for_save1");
        final Tag tag2 = new Tag("tag_for_save2");

        final Collection<Tag> savesTags = tagService.saveAll(Arrays.asList(tag1, tag2));
        assertTrue(tagService.getAllTags().containsAll(savesTags));
    }

    @Test
    public void testGetTracks() {
        final String tag_for_test_get = "tag_for_test_get";
        final Tag tag = tagService.save(tag_for_test_get);
        final Tag expectedTag = tagService.get(tag_for_test_get);

        assertEquals(expectedTag, tag);
    }

    @Test
    public void getTagIsNotExist() {
        assertNull(tagService.get("this_tag_isnt_exist"));
    }

    @Test
    public void testGetTagsLike() {
        final Tag tag1 = new Tag("rock");
        final Tag tag2 = new Tag("indirock");
        final Tag tag3 = new Tag("hard-rock");
        final Tag tag4 = new Tag("jazz");
        tagService.saveAll(Arrays.asList(tag1, tag2, tag3, tag4));
        assertEquals(3, tagService.getAllLike("rock").size());
    }
}
