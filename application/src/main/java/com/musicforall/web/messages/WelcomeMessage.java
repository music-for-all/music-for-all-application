package com.musicforall.web.messages;

/**
 * @author IliaNik on 12.08.2016.
 */

import com.musicforall.util.SecurityUtil;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public final class WelcomeMessage {

    private WelcomeMessage() {
    }

    public static Message create(Session session) throws MessagingException {
        Message message = new MimeMessage(session);

        message.setSubject("Testing Subject"); // header field
        message.setContent("<H1>Welcome to music for all, " + SecurityUtil.currentUser().getUsername()
                + "!</H1>", "text/html");

        return message;
    }
}