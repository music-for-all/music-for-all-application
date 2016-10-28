package com.musicforall.history.service.history;

import com.musicforall.common.dao.HistoryDao;
import com.musicforall.history.handlers.events.EventType;
import com.musicforall.history.model.History;
import com.musicforall.history.service.DBHistoryPopulateService;
import com.musicforall.history.util.ServicesTestConfig;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.AssertTrue;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.musicforall.history.handlers.events.EventType.TRACK_LISTENED;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.omg.PortableServer.IdAssignmentPolicyValue.USER_ID;

/**
 * Created by Pukho on 25.10.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ActiveProfiles("dev")
public class ArtistHistroryServiceTest {

    @Autowired
    private HistoryService service;

    @Autowired
    private ArtistHistoryService artistService;

    @Autowired
    private DBHistoryPopulateService populateService;

    public static final Integer DEFAULT_USER_ID = 1243;

    public static final long CURRENT_TIME = new Date().getTime();

    public static final int UPPER_GAP = 60000;

    public static final int LOW_GAP = 120000;

    public static final List<String> ARTIST_NAMES = Arrays.asList("Pol", "Foals", "Bowie", "Arny");//{"Pol", "Foals", "Bowie"};

    @Before
    public void setUp() {
        populateService.recordArtistHistories(ARTIST_NAMES.subList(0,2), CURRENT_TIME-UPPER_GAP, DEFAULT_USER_ID, EventType.TRACK_LISTENED);
        populateService.recordArtistHistories(ARTIST_NAMES, CURRENT_TIME-LOW_GAP, DEFAULT_USER_ID, EventType.TRACK_LISTENED);
    }

    @Test
    public void testGetStatistic() {

        final DetachedCriteria criteria = DetachedCriteria.forClass(History.class)
                .add(Property.forName("date").between(new Date(CURRENT_TIME-LOW_GAP), new Date(CURRENT_TIME)));

        Collection<History> histories = service.getAllBy(criteria);

        Map<String, Integer> statistic = artistService.getStatistic(new Date(CURRENT_TIME-LOW_GAP), new Date(CURRENT_TIME));
        assertTrue(ARTIST_NAMES.stream()
                .allMatch(f -> {
                    return histories.stream().map(History::getArtistName).filter(f::equals).count() == statistic.get(f);
                }));
}


    @Test
    public void testRecord() {
        artistService.record(artistService.getStatistic(new Date(CURRENT_TIME-UPPER_GAP), new Date(CURRENT_TIME)));
        assertNull(artistService.get(ARTIST_NAMES.get(3)));
        int listened_first = artistService.get(ARTIST_NAMES.get(0)).getPlaysCount();

        artistService.record(artistService.getStatistic(new Date(CURRENT_TIME-LOW_GAP), new Date(CURRENT_TIME-UPPER_GAP)));
        ARTIST_NAMES.forEach(f -> assertNotNull(artistService.get(f)));
        assertTrue(listened_first < artistService.get(ARTIST_NAMES.get(0)).getPlaysCount());
    }
}
