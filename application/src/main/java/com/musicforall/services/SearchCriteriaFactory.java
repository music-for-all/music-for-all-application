package com.musicforall.services;

import com.musicforall.common.Constants;
import com.musicforall.common.query.QueryUtil;
import com.musicforall.model.Artist;
import com.musicforall.model.SearchArtistRequest;
import com.musicforall.model.SearchTrackRequest;
import com.musicforall.model.Track;
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
        final Artist artist = searchCriteria.getArtist();
        final String album = searchCriteria.getAlbum();
        final List<String> tags = searchCriteria.getTags();

        if (title != null && !title.isEmpty()) {
            detachedCriteria.add(Restrictions.ilike("title", QueryUtil.like(title)));
        }
        //Check correctness of using Artist entity
        if (artist != null) {
            detachedCriteria.add(Restrictions.ilike("artist.artistName", QueryUtil.like(artist.getArtistName())));
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

    public static DetachedCriteria createArtistSearchCriteria(SearchArtistRequest searchCriteria) {

        if (searchCriteria == null) {
            return null;
        }
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Artist.class);

        final String artistName = searchCriteria.getArtistName();
        final List<String> tags = searchCriteria.getTags();

        if (artistName != null && !artistName.isEmpty()) {
            detachedCriteria.add(Restrictions.ilike("artistName", QueryUtil.like(artistName)));
        }

        if (tags != null && !tags.isEmpty()) {

            final Disjunction disjunction = Restrictions.disjunction();

            tags.stream().forEach(t -> disjunction.add(Restrictions.eq("tag.name", t).ignoreCase()));

            final DetachedCriteria subcriteria = DetachedCriteria.forClass(Artist.class)
                    .createAlias("tags", "tag")
                    .add(disjunction)
                    .setProjection(Projections.property(Constants.ID));

            detachedCriteria
                    .add(Subqueries.propertyIn(Constants.ID, subcriteria));
        }
        return detachedCriteria;
    }
}