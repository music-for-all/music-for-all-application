package com.musicforall.services;

import com.musicforall.common.query.QueryUtil;
import com.musicforall.model.SearchTrackRequest;
import com.musicforall.model.Track;
import com.musicforall.common.Constants;
import org.hibernate.criterion.*;

import java.util.List;

/**
 * Contains convenience methods to work with search criteria.
 */
public final class SearchCriteriaFactory {

    private SearchCriteriaFactory() {
    }
    public static DetachedCriteria createTrackSearchCriteria(SearchTrackRequest searchCriteria) {

        if (searchCriteria == null) {
            return null;
        }
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Track.class);

        final String title = searchCriteria.getTitle();
        final String artist = searchCriteria.getArtist();
        final String album = searchCriteria.getAlbum();
        final List<String> tags = searchCriteria.getTags();

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

            tags.stream().forEach(t -> disjunction.add(Restrictions.eq("tag.name", t).ignoreCase()));

            final DetachedCriteria subcriteria = DetachedCriteria.forClass(Track.class)
                    .createAlias("tags", "tag")
                    .add(disjunction)
                    .setProjection(Projections.property(Constants.ID));

            detachedCriteria
                    .add(Subqueries.propertyIn(Constants.ID, subcriteria));
        }
        return detachedCriteria;
    }
}