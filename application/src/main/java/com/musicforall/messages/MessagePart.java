package com.musicforall.messages;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author IliaNik on 19.08.2016.
 */
public interface MessagePart {
    MimeMessage getMimeMessage() throws MessagingException;
}
