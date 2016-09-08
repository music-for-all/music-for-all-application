package com.musicforall.services.history;

import com.musicforall.model.Tag;

import java.util.Collection;

/**
 * Created by Andrey on 9/5/16.
 */
public interface PopularHistoryService {

    Collection<Tag> getPopularTags();
}
