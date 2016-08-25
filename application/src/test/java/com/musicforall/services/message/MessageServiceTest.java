package com.musicforall.services.message;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.musicforall.util.ServicesTestConfig;
import freemarker.template.Template;
import org.easymock.EasyMock;
import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.easymock.EasyMock.anyObject;

/**
 * @author IliaNik on 14.08.2016.
 */

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ServicesTestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        MessageTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
@ActiveProfiles("dev")
@PrepareForTest(FreeMarkerTemplateUtils.class)
public class MessageServiceTest {

    private static GreenMail testSmtp;

    @Autowired
    private MailService mailService;

    @Autowired
    private Mails mails;

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
        testSmtp.reset();
    }

    @Test
    @WithUserDetails("user@mail.com")
    public void testSendMessage() throws Exception {
        final String testMessage = "Test email message";

        PowerMock.mockStatic(FreeMarkerTemplateUtils.class);
        EasyMock.expect(FreeMarkerTemplateUtils.processTemplateIntoString(
                anyObject(Template.class),
                anyObject(Map.class)))
                .andReturn(testMessage);
        PowerMock.replayAll();

        mailService.send(mails.welcomeMail());

        final MimeMessage[] messages = testSmtp.getReceivedMessages();
        assertEquals(1, messages.length);

        final String body = GreenMailUtil.getBody(messages[0]);
        assertTrue(body.contains(testMessage));
        PowerMock.verifyAll();
    }

    @Test
    @Ignore
    @WithAnonymousUser
    public void testSendMessageToEmptyUser() throws MessagingException {
        mailService.send(mails.welcomeMail());

        final MimeMessage[] messages = testSmtp.getReceivedMessages();
        assertEquals(0, messages.length);
    }
}