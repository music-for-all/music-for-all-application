package com.musicforall.services.tag;

import com.musicforall.model.Tag;

import java.util.Collection;
import java.util.List;

/**
 * Created by Pukho on 22.06.2016.
 */
public interface TagService {

    Tag save(String name);

    Collection<Tag> saveAll(Collection<Tag> tags);

    Tag get(String name);

    List<Tag> getAllTags();

    List<Tag> getAllLike(String tagName);

    List<Tag> getTheMostPopularTags();
}
