package com.musicforall.services.tag;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Tag;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by Pukho on 22.06.2016.
 */
@Service("tagService")
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private Dao dao;

    @Override
    public Tag save(String name) {
        return dao.save(new Tag(name));
    }

    @Override
    public boolean isTagExist(String name) {
        return this.get(name) != null;
    }

    @Override
    public Tag get(String name) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tag.class)
                .add(Property.forName("name").eq(name));
        return dao.getBy(detachedCriteria);
    }

    @Override
    public List<Tag> getAllTags() {
        return dao.all(Tag.class);
    }

    @Override
    public Collection<Tag> saveAll(Collection<Tag> tags) {
        return dao.saveAll(tags);
    }

}
