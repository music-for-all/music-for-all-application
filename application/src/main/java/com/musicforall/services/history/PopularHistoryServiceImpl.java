package com.musicforall.services.history;

import com.musicforall.common.dao.Dao;
import com.musicforall.common.dao.QueryParams;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Andrey on 9/5/16.
 */
@Service("popularService")
@Transactional
public class PopularHistoryServiceImpl implements PopularHistoryService {

    @Autowired
    private Dao dao;

    @Autowired
    private HistoryService historyService;

    @Override
    public Collection<Tag> getPopularTags() {
        final List<Integer> ids = historyService.getMostPopularTracks();
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        final int count = 20;
        final int offset = 0;

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("ids", ids);
        final Collection<Tag> tags = dao.getAllByNamedQuery(Tag.class, Tag.POPULAR_TAGS_BY_TRACK_ID_QUERY,
                parameters, new QueryParams(count, offset));
        return tags;
    }
}
