package com.musicforall.services.message;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.musicforall.services.MessageService;
import com.musicforall.util.MessageUtil;
import com.musicforall.util.ServicesTestConfig;
import freemarker.template.Configuration;
import org.easymock.EasyMock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

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
@PrepareForTest(MessageUtil.class)
public class MessageServiceTest {

    private static GreenMail testSmtp;
    @Autowired
    private MessageService messageService;

    @BeforeClass
    public static void testSmtpInit() {
        testSmtp = new GreenMail(ServerSetupTest.SMTP);
        testSmtp.start();
    }

    @AfterClass
    public static void cleanup() {
        testSmtp.stop();
    }

    @Test
    @WithUserDetails("user")
    public void testEmail() throws MessagingException {
        final String testMessage = "Test email message";

        PowerMock.mockStatic(MessageUtil.class);
        EasyMock.expect(MessageUtil.createMessageFromTemplate(
                anyObject(Configuration.class),
                anyObject(Map.class),
                anyObject(String.class)))
                .andReturn(testMessage);
        PowerMock.replayAll();

        messageService.sendWelcomeMessage();

        final MimeMessage[] messages = testSmtp.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("Music For All", messages[0].getSubject());

        final String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");
        assertTrue(body.contains(testMessage));
        PowerMock.verifyAll();
    }
}