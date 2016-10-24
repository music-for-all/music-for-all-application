package com.musicforall.common.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by kgavrylchenko on 10/23/2015.
 */
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Dao {
    public static final String FOUND_ENTITY = "Found entity - {}";
    private static final Logger LOG = LoggerFactory.getLogger(Dao.class);
    private final int batchSize = 20;

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Return the persistent instance of the given entity class with the given identifier,
     * or null if there is no such persistent instance.
     *
     * @param entityClass a persistent class
     * @param id          an identifier
     * @return a persistent instance or null
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> entityClass, Serializable id) {
        final T entity = currentSession().get(entityClass, id);
        LOG.info("Retrieved entity - {}", entity);
        return entity;
    }

    /**
     * Return all persistent instances of the given entity class,
     * or null if there is no such persistent instance.
     *
     * @param entityClass a persistent class
     * @return List of persistent instances or null
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> all(Class<T> entityClass) {
        final List<T> entities = currentSession().createCriteria(entityClass).list();
        LOG.info("Retrieved entites - {}", entities);
        return entities;
    }

    /*
     * Persist the given transient instance.
     *
     * @param entity a transient instance of a persistent class
     * @return saved entity
     */
    public <T> T save(T entity) {
        LOG.info("Going to save entity - {}", entity);
        currentSession().saveOrUpdate(entity);
        return entity;
    }

    public <T> T update(T entity) {
        LOG.info("Going to update entity - {}", entity);
        currentSession().update(entity);
        return entity;
    }

    public void update(String namedQuery, Map<String, Object> parameters) {
        LOG.info("Going to update entity by named query - {} with parameters - {}", namedQuery, parameters);
        final Query query = currentSession().createNamedQuery(namedQuery);
        for (final Map.Entry<String, Object> s : parameters.entrySet()) {
            query.setParameter(s.getKey(), s.getValue());
        }
        query.executeUpdate();
    }

    /**
     * Persists collection of entities wrapped in list
     *
     * @param entities - list entities to save
     */
    public <T> Collection<T> saveAll(Collection<T> entities) {
        LOG.info("Going to save entities - {}", entities);
        final Session session = currentSession();
        final List<T> entitiesList = new ArrayList<>(entities);
        for (int i = 0; i < entities.size(); i++) {
            session.save(entitiesList.get(i));
            if (i % batchSize == 0) {
                session.flush();
                session.clear();
            }
        }
        return entitiesList;
    }

    /**
     * Remove a persistent instance from the datastore.
     *
     * @param entity - the instance to be removed
     */

    public <T> void delete(T entity) {
        LOG.info("Going to delete entity - {}", entity);
        currentSession().delete(entity);
    }

    /**
     * Return the persistent instance of the given entity class with the given parameters,
     * with NamedQuery annotation
     *
     * @param clazz      type of return class entity
     * @param namedQuery - name of query
     * @param parameters - pairs of key/value parameters which will be added to "where" part of the query
     * @return a persistent instance or null
     */

    public <T> T getByNamedQuery(Class<T> clazz, String namedQuery, Map<String, Object> parameters) {
        LOG.info("Going to find entities with class - {} by named query - {} with parameters - {}",
                clazz, namedQuery, parameters);
        final Query<T> query = currentSession().createNamedQuery(namedQuery, clazz);
        query.setProperties(parameters);
        final T entity = query.uniqueResult();
        LOG.info(FOUND_ENTITY, entity);
        return entity;
    }

    /**
     * Return the persistent instances of the given entity class with the given parameters,
     *
     * @param clazz       type of return class entity
     * @param namedQuery  - name of query
     * @param parameters  - pairs of key/value parameters which will be added to "where" part of the query
     * @param queryParams - list of QueryParams parameters
     * @return list of persistent instances or null
     */

    public <T> List<T> getAllByNamedQuery(Class<T> clazz, String namedQuery,
                                          Map<String, Object> parameters, QueryParams queryParams) {
        LOG.info("Going to find entities with class - {} by named query - {} with parameters - {}",
                clazz, namedQuery, parameters);
        final Query<T> query = currentSession().createNamedQuery(namedQuery, clazz);
        query.setProperties(parameters);
        query.setMaxResults(queryParams.getMaxCount());
        query.setFirstResult(queryParams.getOffset());
        final List<T> entities = query.list();
        LOG.info(FOUND_ENTITY, entities);
        return entities;
    }

    /**
     * Return the persistent instance of the given entity class with the given parameters,
     * It uses Criterians. If there are there several entities that meet given parameters, exception is thrown.
     *
     * @param criteria criterion to match the search against, for creations of Criterion use
     *                 Restrictions class e.g Restrictions.eq(propertyName, value)
     * @return a persistent instance or null
     */
    public <T> T getBy(DetachedCriteria criteria) {
        LOG.info("Going to find entity by criteria - {}", criteria);
        final Criteria executableCriteria = criteria.getExecutableCriteria(currentSession());
        final T entity = (T) executableCriteria.uniqueResult();
        LOG.info(FOUND_ENTITY, entity);
        return entity;
    }


    /**
     * Return the persistent instances of the given entity class with the given parameters,
     * It uses Criterians.
     *
     * @param criteria criterion to match the search against, for creations of Criterion use
     *                 Restrictions class e.g Restrictions.eq(propertyName, value)
     * @return list of persistent instances or null
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getAllBy(DetachedCriteria criteria) {
        LOG.info("Going to find entities by criteria - {}", criteria);
        final Criteria executableCriteria = criteria.getExecutableCriteria(currentSession());
        final List<T> entities = executableCriteria.list();
        LOG.info(FOUND_ENTITY, entities);
        return entities;
    }

    /**
     * Return the persistent instances of the given entity class with the given parameters,
     *
     * @param clazz      type of return class entity
     * @param namedQuery - name of query
     * @param params     - pairs of key/value parameters which will be added to "where" part of the query
     * @return list of persistent instances or null
     */

    public <T> List<T> getAllByNamedQuery(Class<T> clazz, String namedQuery, Map<String, Object> params) {
        LOG.info("Going to find entities with class - {} by named query - {} with parameters - {}",
                clazz, namedQuery, params);
        final Query<T> query = currentSession().createNamedQuery(namedQuery, clazz);
        query.setProperties(params);
        final List<T> entities = query.list();
        LOG.info(FOUND_ENTITY, entities);
        return entities;
    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
