package com.musicforall.services.tag;

import com.musicforall.common.dao.Dao;
import com.musicforall.common.dao.QueryParams;
import com.musicforall.common.query.QueryUtil;
import com.musicforall.history.service.history.HistoryService;
import com.musicforall.model.Tag;
import com.musicforall.common.Constants;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Pukho on 22.06.2016.
 */
@Service("tagService")
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private Dao dao;

    @Autowired
    private HistoryService historyService;

    @Override
    public Tag save(String name) {
        return dao.save(new Tag(name));
    }

    @Override
    public Tag get(String name) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tag.class)
                .add(Property.forName(Constants.NAME).eq(name));
        return dao.getBy(detachedCriteria);
    }

    @Override
    public List<Tag> getAllTags() {
        return dao.all(Tag.class);
    }

    @Override
    public List<Tag> getAllLike(String tagName) {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(Constants.NAME, QueryUtil.like(tagName.toLowerCase()));
        return dao.getAllByNamedQuery(Tag.class, Tag.ALL_LIKE_NAME_QUERY, properties);
    }

    @Override
    public Collection<Tag> saveAll(Collection<Tag> tags) {
        return dao.saveAll(tags);
    }

    @Override
    public List<String> getTheMostPopularTags() {
        final List<Integer> ids = historyService.getTheMostPopularTracks();

        final int count = 20;
        final int offset = 0;

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("ids", ids);
        final List<String> tags = dao.getAllByNamedQuery(String.class, Tag.POPULAR_TAGS_QUERY,
                parameters, new QueryParams(count, offset));
        return tags;
    }
}
