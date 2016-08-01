package com.musicforall.services.tag;

import com.musicforall.model.Tag;

import java.util.Collection;
import java.util.List;

/**
 * Created by Pukho on 22.06.2016.
 */
public interface TagService {

    /**
     * Creates a new tag with the given name.
     * @param name the name for the new tag
     * @return an instance of the newly created Tag
     */
    Tag save(String name);

    /**
     * Saves the given collection of tags in the database.
     * @param tags the collection of tags to be saved
     * @return a collection of saved tags
     */
    Collection<Tag> saveAll(Collection<Tag> tags);

    /**
     * Retrieves a Tag with the provided name.
     * @param name the name of the tag
     * @return an instance of the retrieved Tag
     */
    Tag get(String name);

    /**
     * Gets a collection of all tags recorded in the database.
     * @return a collection of type Tag
     */
    List<Tag> getAllTags();
}
