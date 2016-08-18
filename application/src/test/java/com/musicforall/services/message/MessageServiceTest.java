
package com.musicforall.services.message;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.musicforall.services.MessageService;
import com.musicforall.util.SecurityUtil;
import com.musicforall.util.ServicesTestConfig;
import com.musicforall.web.messages.WelcomeMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


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
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    private GreenMail testSmtp;

    @Before
    public void testSmtpInit() {
        testSmtp = new GreenMail(ServerSetupTest.SMTP);
        testSmtp.start();

        messageService.setPort(3025);
        messageService.setHost("localhost");
    }


    @Test
    @WithUserDetails("user")
    public void testEmail() throws InterruptedException, MessagingException {
        messageService.sendWelcomeMessage();

        MimeMessage[] messages = testSmtp.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("Music For All", messages[0].getSubject());
        String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");

        String pattern = "(.*)(" +
                WelcomeMessage.getWelcomeText(SecurityUtil.currentUser().getUsername()) + ")(.*)";

        Matcher m = Pattern.compile(pattern).matcher(body);

        assertTrue(m.find());
    }


    @After
    public void cleanup() {
        testSmtp.stop();

        messageService.setPort(25);
        messageService.setHost("smtp.gmail.com");
    }
}