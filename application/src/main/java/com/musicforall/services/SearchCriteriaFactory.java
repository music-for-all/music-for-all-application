package com.musicforall.services;

import com.musicforall.common.query.QueryUtil;
import com.musicforall.model.SearchCriteria;
import com.musicforall.model.Track;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

public final class SearchCriteriaFactory {

    private SearchCriteriaFactory() {
    }

    public static DetachedCriteria buildTrackSearchCriteria(SearchCriteria searchCriteria) {
        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Track.class);
        if (searchCriteria.getTitle() != null) {
            detachedCriteria.add(Restrictions.ilike("name", QueryUtil.like(searchCriteria.getTitle())));
        }
        if (searchCriteria.getArtist() != null) {
            detachedCriteria.add(Restrictions.ilike("artist", QueryUtil.like(searchCriteria.getArtist())));
        }
        if (searchCriteria.getAlbum() != null) {
            detachedCriteria.add(Restrictions.ilike("album", QueryUtil.like(searchCriteria.getAlbum())));
        }
        if (searchCriteria.getTags() != null) {
            final Disjunction disjunction = Restrictions.disjunction();
            searchCriteria.getTags().stream()
                    .map(QueryUtil::like)
                    .map(likeTag -> Restrictions.ilike("name", likeTag)).forEach(disjunction::add);
            detachedCriteria.createCriteria("tags").add(disjunction);
            detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        }
        return detachedCriteria;
    }
}
