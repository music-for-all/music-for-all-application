package com.musicforall.services;

import com.musicforall.common.query.QueryUtil;
import com.musicforall.model.Track;
import com.musicforall.model.TrackSearch;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

public final class SearchCriteriaFactory {

    private SearchCriteriaFactory() {
    }

    public static DetachedCriteria buildTrackSearchCriteria(TrackSearch trackSearch) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Track.class);
        if (trackSearch.getTitle() != null) {
            detachedCriteria.add(Restrictions.ilike("name", QueryUtil.like(trackSearch.getTitle())));
        }
        if (trackSearch.getArtist() != null) {
            detachedCriteria.add(Restrictions.ilike("artist", QueryUtil.like(trackSearch.getArtist())));
        }
        if (trackSearch.getAlbum() != null) {
            detachedCriteria.add(Restrictions.ilike("album", QueryUtil.like(trackSearch.getAlbum())));
        }
        if (trackSearch.getTags() != null) {
            final Disjunction disjunction = Restrictions.disjunction();
            trackSearch.getTags().stream()
                    .map(QueryUtil::like)
                    .map(likeTag -> Restrictions.ilike("name", likeTag)).forEach(disjunction::add);
            detachedCriteria.createCriteria("tags").add(disjunction);
            detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        }
        return detachedCriteria;
    }
}
