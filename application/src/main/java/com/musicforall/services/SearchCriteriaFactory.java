package com.musicforall.services;

import com.musicforall.common.query.QueryUtil;
import com.musicforall.model.SearchCriteria;
import com.musicforall.model.Track;
import org.hibernate.criterion.*;

import java.util.List;

/**
 * Contains convenience methods to work with search criteria.
 */
public final class SearchCriteriaFactory {

    private SearchCriteriaFactory() {
    }

    /**
     * Creates a detached criteria based on the data provided in a SearchCriteria object.
     * @param searchCriteria an instance of the SearchCriteria class
     * @return the detached criteria
     */
    public static DetachedCriteria buildTrackSearchCriteria(SearchCriteria searchCriteria) {

        if (searchCriteria == null) {
            return null;
        }
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

            for (String tagName: tags) {
                disjunction.add(Restrictions.eq("tag.name", tagName).ignoreCase());
            }
            final DetachedCriteria subcriteria = DetachedCriteria.forClass(Track.class)
                    .createAlias("tags", "tag")
                    .add(disjunction)
                    .setProjection(Projections.property("id"));

            detachedCriteria
                    .add(Subqueries.propertyIn("id", subcriteria));
        }
        return detachedCriteria;
    }
}