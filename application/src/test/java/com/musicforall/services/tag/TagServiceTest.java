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
import java.util.HashSet;
import java.util.List;

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
    public void testTagsBasic(){
        tagService.save("pop");
        tagService.save(new HashSet<>(Arrays.asList(new Tag("soul"), new Tag("alternative"))));

        assertTrue(tagService.isTagExist("pop"));
        assertNotNull(tagService.get("soul"));

        assertFalse(tagService.isTagExist("pop_not_exist"));
        assertNull(tagService.get("soul_not_exist"));

        List<Tag> allTags = tagService.getAllTags();
        assertTrue(allTags.contains(tagService.get("alternative")));
        assertFalse(allTags.contains(tagService.get("soul_not_exist")));
    }
}
