package com.musicforall.services.message;

import com.musicforall.services.MessageService;
import com.musicforall.util.ServicesTestConfig;
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


    @Test
    @WithUserDetails("user")
    public void testSendMessageToRealEmail() throws MessagingException {
        assertTrue(messageService.sendWelcomeMessage());

    }

    @Test(expected = MessagingException.class)
    @WithUserDetails("email_not_exist")
    public void testSendMessageToUnrealEmail() throws MessagingException {
        messageService.sendWelcomeMessage();
    }
}
