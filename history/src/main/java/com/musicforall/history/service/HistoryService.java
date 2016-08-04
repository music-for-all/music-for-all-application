package com.musicforall.history.service;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.model.History;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kgavrylchenko on 25.07.16.
 */
@Service
@Transactional
public class HistoryService {

    @Autowired
    private Dao dao;

    public void record(History history) {
        dao.save(history);
    }

    public History getBy(final DetachedCriteria criteria) {
        return dao.getBy(criteria);
    }
}