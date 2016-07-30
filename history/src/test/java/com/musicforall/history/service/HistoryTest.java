package com.musicforall.history.service;

import com.musicforall.history.handlers.HistoryEventListener;
import com.musicforall.history.handlers.events.TrackListenedEvent;
import com.musicforall.history.model.History;
import com.musicforall.history.util.ServicesTestConfig;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author IliaNik on 29.07.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
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
    private HistoryService historyService;

    @Spy
    @Autowired
    private HistoryEventListener historyEventListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenUsingTheSpyAnnotation_thenObjectIsSpied() {
        final TrackListenedEvent event = new TrackListenedEvent(TRACK_ID, USER_ID);

//        publisher.publishEvent(event);
        historyEventListener.handleTrackListened(event);
        Mockito.verify(historyEventListener).handleTrackListened(event);

        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(History.class)
                .add(Property.forName("userId").eq(USER_ID))
                .add(Property.forName("trackId").eq(TRACK_ID));
        final List<History> history = historyService.getBy(detachedCriteria);
        assertTrue(!history.isEmpty());

    }

}
