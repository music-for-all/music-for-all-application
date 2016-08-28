package com.musicforall.services.message;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.musicforall.services.template.TemplateService;
import com.musicforall.util.ServicesTestConfig;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.mail.internet.MimeMessage;

import static com.musicforall.util.SecurityUtil.currentUser;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * @author IliaNik on 14.08.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        MessageTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
@ActiveProfiles("dev")
@WithUserDetails("user@mail.com")
public class MailServiceTest {

    private static GreenMail testSmtp;

    @Autowired
    private MailService mailService;

    @Autowired
    @InjectMocks
    private Mails mails;

    @Mock
    private TemplateService templateService;

    @BeforeClass
    public static void testSmtpInit() {
        testSmtp = new GreenMail(ServerSetupTest.SMTP);
    }

    @AfterClass
    public static void cleanup() {
        testSmtp.stop();
    }

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        testSmtp.reset();
    }

    @Test
    public void testSendMessage() throws Exception {
        final String testMessage = "Test email message";

        when(templateService.from(any(), any())).thenReturn(testMessage);
        mailService.send(mails.welcomeMail(currentUser()));

        final MimeMessage[] messages = testSmtp.getReceivedMessages();
        assertEquals(1, messages.length);

        final String body = GreenMailUtil.getBody(messages[0]);
        assertTrue(body.contains(testMessage));
    }
}