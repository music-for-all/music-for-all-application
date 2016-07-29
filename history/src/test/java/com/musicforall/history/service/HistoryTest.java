package com.musicforall.history.service;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.handlers.HistoryEventListener;
import com.musicforall.history.handlers.events.TrackListenedEvent;
import com.musicforall.history.model.History;
import com.musicforall.history.util.ServicesTestConfig;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author IliaNik on 29.07.2016.
 */

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        HistoryTestExecutionListener.class})
@ActiveProfiles("dev")
public class HistoryTest {

    static final Integer USER_ID = 1111;
    static final Integer TRACK_ID = 2222;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private Dao dao;

    private TrackListenedEvent trackListenedEvent;

    @Spy
    @Autowired
    private HistoryEventListener historyEventListener;

    @Test
    public void whenUsingTheSpyAnnotation_thenObjectIsSpied() {

        trackListenedEvent = new TrackListenedEvent(TRACK_ID, USER_ID);

        publisher.publishEvent(trackListenedEvent);

        Mockito.verify(historyEventListener).handleTrackListened(trackListenedEvent);

        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(History.class)
                .add(Property.forName("user_id").eq(USER_ID))
                .add(Property.forName("track_id").eq(TRACK_ID));
        final List<History> history = dao.getAllBy(detachedCriteria);
        assertTrue(!history.isEmpty());

    }

}
