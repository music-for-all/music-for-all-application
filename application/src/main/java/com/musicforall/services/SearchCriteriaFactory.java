package com.musicforall.services;

import com.musicforall.common.Constants;
import com.musicforall.common.query.QueryUtil;
import com.musicforall.model.*;
import com.musicforall.model.user.UserData;
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

        final String name = searchCriteria.getTrackName();
        final Artist artist = searchCriteria.getArtist();
        final String album = searchCriteria.getAlbum();
        final List<String> tags = searchCriteria.getTags();

        if (name != null && !name.isEmpty()) {
            detachedCriteria.add(Restrictions.ilike("name", QueryUtil.like(name)));
        }
        //Check correctness of using Artist entity
        if (artist != null) {
            detachedCriteria.add(Restrictions.ilike("artist.name", QueryUtil.like(artist.getName())));
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
            detachedCriteria.add(Restrictions.ilike("name", QueryUtil.like(artistName)));
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

    public static DetachedCriteria createUserSearchCriteria(SearchUserRequest searchCriteria) {

        if (searchCriteria == null) {
            return null;
        }
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserData.class);

        final String username = searchCriteria.getUsername();
        final String firstName = searchCriteria.getFirstName();
        final String lastName = searchCriteria.getLastName();

        if (username != null && !username.isEmpty()) {
            detachedCriteria.add(Restrictions.ilike("username", QueryUtil.like(username)));
        }

        if (firstName != null && !firstName.isEmpty()) {
            detachedCriteria.add(Restrictions.ilike("firstName", QueryUtil.like(firstName)));
        }

        if (lastName != null && !lastName.isEmpty()) {
            detachedCriteria.add(Restrictions.ilike("lastName", QueryUtil.like(lastName)));
        }

        return detachedCriteria;
    }
}