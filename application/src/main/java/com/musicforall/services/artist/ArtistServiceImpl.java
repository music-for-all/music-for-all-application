package com.musicforall.services.artist;

import com.musicforall.common.dao.Dao;
import com.musicforall.common.query.QueryUtil;
import com.musicforall.model.Artist;
import com.musicforall.model.Tag;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pavel Podgorniy on 8/19/2016.
 */
@Service("artistService")
@Transactional
public class ArtistServiceImpl implements ArtistService{
    @Autowired
    private Dao dao;

    @Override
    public Artist save(Artist artist) {
        return dao.save(artist);
    }

    @Override
    public Artist get(String name) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Artist.class)
                .add(Property.forName("artistName").eq(name));
        return dao.getBy(detachedCriteria);
    }

    @Override
    public List<Artist> getAllArtists() {
        return dao.all(Artist.class);
    }

    @Override
    public Collection<Artist> saveAll(Collection<Artist> artists) {
        return dao.saveAll(artists);
    }
}
