package com.musicforall.services.template;

import com.musicforall.util.ServicesTestConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author ENikolskiy.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
})
@ActiveProfiles("dev")
public class TemplateServiceTest {
    private final static List<String> WORDS = unmodifiableList(Arrays.asList("key", "value", "test", "user"));
    private final static URL url = TemplateServiceTest.class.getClassLoader().getResource("test.html");

    @Autowired
    private TemplateService templateService;

    @Test
    public void testFrom() throws Exception {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("words", WORDS);

        final String html = templateService.from("template.ftl", params);
        final Document template = Jsoup.parse(html);
        final Document expectedHtml = Jsoup.parse(new File(url.toURI()), "UTF-8");

        int nodeSize = template.childNodes().size();
        assertEquals(nodeSize, expectedHtml.childNodeSize());

        for (int i = 0; i < nodeSize; i++) {
            assertTrue(template.childNode(i).hasSameValue(expectedHtml.childNode(i)));
        }
    }
}