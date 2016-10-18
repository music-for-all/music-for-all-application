package com.musicforall.common.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Pukho on 16.10.2016.
 */
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HistoryDao extends AbstractDao {

    public static final String FOUND_ENTITY = "Found entity - {}";
    private static final Logger LOG = LoggerFactory.getLogger(Dao.class);
    private final int batchSize = 20;

    @Autowired
    @Qualifier("history_session")
    private SessionFactory sessionFactory;


    @Override
    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
