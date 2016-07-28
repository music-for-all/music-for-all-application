package com.musicforall.history.service;

import com.musicforall.common.dao.Dao;
import com.musicforall.history.handlers.HistoryEventListener;
import com.musicforall.history.handlers.events.TrackListenedEvent;
import com.musicforall.history.model.History;
import com.musicforall.history.util.ServicesTestConfig;
import org.easymock.EasyMock;
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

import static org.easymock.EasyMock.anyObject;
import static org.junit.Assert.assertNotNull;

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

    static final Integer User_ID = 1111;

    static final Integer Track_ID = 2222;

    @Autowired
    private Dao dao;

    @Test
    public void testEventListener() {

        TrackListenedEvent trackListenedEvent = new TrackListenedEvent(Track_ID, User_ID);


        HistoryEventListener historyEventListener = PowerMock.createMock(HistoryEventListener.class);

        historyEventListener.handleTrackListened(trackListenedEvent);
        EasyMock.expectLastCall().atLeastOnce();
        EasyMock.replay(historyEventListener);

        historyEventListener.handleTrackListened(trackListenedEvent);
        EasyMock.verify(historyEventListener);

    }


    @Test
    public void testEventService() {

        TrackListenedEvent trackListenedEvent = new TrackListenedEvent(Track_ID, User_ID);

        HistoryEventListener historyEventListener = new HistoryEventListener();

        HistoryService historyService = EasyMock.createMock(HistoryService.class);

        historyService.record(anyObject(History.class));
        EasyMock.expectLastCall().atLeastOnce();
        EasyMock.replay(historyService);

        historyEventListener.handleTrackListened(trackListenedEvent);
        EasyMock.verify(historyService);

        final History history = dao.get(History.class, savedHistory.getId());
        assertNotNull(history);
    }
}
