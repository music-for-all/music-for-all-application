package org.exschool.pocketworld.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * Created by kgavrylchenko on 10/23/2015.
 */
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Dao {
    private static final Logger LOG = LoggerFactory.getLogger(Dao.class);

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
        T entity = currentSession().get(entityClass, id);
        LOG.info("retrieved entity - {}", entity);
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
        List<T> entities = currentSession().createCriteria(entityClass).list();
        LOG.info("retrieved entites - {}", entities);
        return entities;
    }

    /**
     * Persist the given transient instance.
     *
     * @param entity a transient instance of a persistent class
     * @return saved entity
     */
    public <T> T save(T entity) {
        LOG.info("going to save entity - {}", entity);
        currentSession().saveOrUpdate(entity);
        return entity;
    }

    /**
     * Persists collection of entities wrapped in list
     *
     * @param entities - list entities to save
     */
    public <T> Collection<T> saveAll(Collection<T> entities) {
        LOG.info("going to save entities - {}", entities);
        Session session = currentSession();
        List<T> entitiesList = new ArrayList<>(entities);
        for (int i = 0; i < entities.size(); i++) {
            session.save(entitiesList.get(i));
            if (i % 20 == 0) {
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
        LOG.info("going to delete entity - {}", entity);
        currentSession().delete(entity);
    }

    /**
     * Return the persistent instance of the given entity class with the given parameters,
     * It uses HQL. If there are there several entities that meet given parameters, returns first item.
     *
     * @param hql        a hql query
     * @param parameters pairs of key/value parameters which will be added to "where" part of the query
     * @return a persistent instance or null
     */
    public <T> T getBy(String hql, Map<String, String> parameters) {
        LOG.info("going to find entity by hql - {}, with parameters - {}", hql, parameters);
        Query query = currentSession().createQuery(hql);
        query.setProperties(parameters);
        T entity =  (T) query.uniqueResult();
        LOG.info("Found entity - {}", entity);
        return entity;
    }
        
    public void update(String sql, Map<String, List<Serializable>> parametrs){    		
    	Query query = currentSession().createSQLQuery(sql);    	
    	for (Entry<String, List<Serializable>> s : parametrs.entrySet())
    		query.setParameterList(s.getKey(), s.getValue());
    	
    	query.executeUpdate();
    }

    /**
     * Return the persistent instances of the given entity class which meet given parameters,
     *
     * @param hql        a hql query
     * @param parameters pairs of key/value parameters which will be added to "where" part of the query
     * @return list of persistent instances or null
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getAllBy(String hql, Map<String, String> parameters) {
        LOG.info("going to find entities by hql - {}, with parameters - {}", hql, parameters);
        Query query = currentSession().createQuery(hql);
        query.setProperties(parameters);
        List<T> entities = (List<T>) query.list();
        LOG.info("Found entities - {}", entities);
        return entities;
    }

    ;

    /**
     * Return the persistent instance of the given entity class with the given parameters,
     * It uses Criterians. If there are there several entities that meet given parameters, returns first item.
     *
     * @param criteria criterion to match the search against, for creations of Criterion use
     *                   Restrictions class e.g Restrictions.eq(propertyName, value)
     * @return a persistent instance or null
     */
    public <T> T getBy(DetachedCriteria criteria) {
        LOG.info("going to find entity by criteria - {}", criteria);
        Criteria executableCriteria = criteria.getExecutableCriteria(currentSession());
        T entity = (T) executableCriteria.uniqueResult();
        LOG.info("Found entity - {}", entity);
        return entity;
    }


    /**
     * Return the persistent instances of the given entity class with the given parameters,
     * It uses Criterians.
     *
     * @param criteria criterion to match the search against, for creations of Criterion use
     *                   Restrictions class e.g Restrictions.eq(propertyName, value)
     * @return list of persistent instances or null
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getAllBy(DetachedCriteria criteria) {
        LOG.info("going to find entities by criteria - {}", criteria);
        Criteria executableCriteria = criteria.getExecutableCriteria(currentSession());
        List<T> entities = executableCriteria.list();
        LOG.info("Found entities - {}", entities);
        return entities;
    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
