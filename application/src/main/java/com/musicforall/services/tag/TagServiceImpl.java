package com.musicforall.services.tag;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Tag;
import com.musicforall.model.Track;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Pukho on 22.06.2016.
 */
@Service("tagService")
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private Dao dao;

    @Override
    public void save(String name) {
        dao.save(new Tag(name));
    }

    @Override
    public boolean isTagExist(String name) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tag.class)
                .add(Property.forName("name").eq(name));
        return dao.getBy(detachedCriteria) != null;
    }

    @Override
    public List<Tag> getAllTags() {
        return dao.all(Tag.class);
    }

    @Override
    public void save(Set<Tag> tags) {
        for (Tag tag:
             tags) {
            if (!isTagExist(tag.getName()))
                dao.save(tag);
        }
    }

}
