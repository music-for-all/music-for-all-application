package com.musicforall.services;

import com.musicforall.common.dao.Dao;
import com.musicforall.model.Song;
import com.musicforall.model.Tag;
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
    public Tag isTagExist(String name) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tag.class)
                .add(Property.forName("name").eq(name));
        return dao.getBy(detachedCriteria);
    }

    @Override
    public List<Tag> getAllTags() {
        return dao.all(Tag.class);
    }

    @Override
    public void addTag(Integer songId, Set<Tag> tags) {
        Song song = dao.get(Song.class, songId);

         for (Tag tag:
             tags) {
            if (this.isTagExist(tag.getName()) == null) {
                dao.save(tag);
            }

            if (song.getTags()==null)  song.setTags(new HashSet<Tag>());
            song.getTags().add(tag);
            dao.save(song);
        }
    }
}
