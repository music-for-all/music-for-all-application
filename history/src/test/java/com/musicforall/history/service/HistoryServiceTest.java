package com.musicforall.history.service;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.handlers.HistoryEventListener;
import com.musicforall.history.handlers.events.TrackListenedEvent;
import com.musicforall.history.model.History;
import com.musicforall.history.util.ServicesTestConfig;
import org.easymock.EasyMock;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.easymock.EasyMock.anyObject;
import static org.junit.Assert.assertTrue;


/**
 * @author IliaNik on 28.07.2016.
 */

@RunWith(PowerMockRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        HistoryTestExecutionListener.class})
@ActiveProfiles("dev")
public class HistoryServiceTest {

    static final Integer USER_ID = 1111;

    static final Integer TRACK_ID = 2222;

    @Autowired
    private Dao dao;


    private TrackListenedEvent trackListenedEvent;
    private HistoryEventListener historyEventListener;
    private HistoryService historyService;

    @Before
    public void setUp() {
        trackListenedEvent = new TrackListenedEvent(TRACK_ID, USER_ID);

        historyEventListener = PowerMock.createPartialMock(HistoryEventListener.class,
                "toHistoryEntity", new Class[]{TrackListenedEvent.class});

        historyService = EasyMock.createMock(HistoryService.class);
    }


    @Test
    public void test() throws Exception {
        PowerMock.expectPrivate(historyEventListener, "toHistoryEntity",
                anyObject(History.class)).andReturn(new History());


        historyService.record(anyObject(History.class));
        EasyMock.expectLastCall();


        EasyMock.replay(historyService, historyEventListener);

        historyEventListener.handleTrackListened(trackListenedEvent);

        EasyMock.verify(historyService, historyEventListener);


        final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(History.class)
                .add(Property.forName("user_id").eq(USER_ID))
                .add(Property.forName("track_id").eq(TRACK_ID));
        final List<History> history = dao.getAllBy(detachedCriteria);
        assertTrue(!history.isEmpty());
    }
}
