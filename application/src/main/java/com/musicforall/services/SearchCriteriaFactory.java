package com.musicforall.services;

import com.musicforall.common.query.QueryUtil;
import com.musicforall.model.SearchCriteria;
import com.musicforall.model.Track;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public final class SearchCriteriaFactory {

    private SearchCriteriaFactory() {
    }

    /**
     * Creates a detached criteria based on the data provided in a SearchCriteria object.
     * @param searchCriteria an instance of the SearchCriteria class
     * @return the detached criteria
     */
    public static DetachedCriteria buildTrackSearchCriteria(SearchCriteria searchCriteria) {

        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Track.class);

        String title = searchCriteria.getTitle();
        String artist = searchCriteria.getArtist();
        String album = searchCriteria.getAlbum();
        List<String> tags = searchCriteria.getTags();

        if (title != null && !title.isEmpty()) {
            detachedCriteria.add(Restrictions.ilike("title", QueryUtil.like(title)));
        }
        if (artist != null && !artist.isEmpty()) {
            detachedCriteria.add(Restrictions.ilike("artist", QueryUtil.like(artist)));
        }
        if (album != null && !album.isEmpty()) {
            detachedCriteria.add(Restrictions.ilike("album", QueryUtil.like(album)));
        }
        if (tags != null && !tags.isEmpty()) {
            final Disjunction disjunction = Restrictions.disjunction();
            tags.stream()
                    .map(QueryUtil::like)
                    .map(likeTag -> Restrictions.ilike("name", likeTag)).forEach(disjunction::add);
            detachedCriteria.createCriteria("tags").add(disjunction);
            detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        }
        return detachedCriteria;
    }
}