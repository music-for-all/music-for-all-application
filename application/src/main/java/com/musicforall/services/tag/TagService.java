package com.musicforall.services.tag;

import com.musicforall.model.Tag;

import java.util.List;
import java.util.Set;

/**
 * Created by Pukho on 22.06.2016.
 */
public interface TagService {
    void save(String name);

    Tag isTagExist(String name);

    List<Tag> getAllTags();

    void addTag(Integer songId, Set<Tag> tags);
}
