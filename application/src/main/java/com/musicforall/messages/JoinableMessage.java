package com.musicforall.messages;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Evgeniy on 21.08.2016.
 */
public abstract class JoinableMessage implements MessagePart {

    private MessagePart part;

    @Override
    public MimeMessage getMimeMessage() throws MessagingException {
        return decorate(part.getMimeMessage());
    }

    protected abstract MimeMessage decorate(MimeMessage mimeMessage) throws MessagingException;

    public MessageRoot root(final MessageRoot root) {
        this.part = root;
        return root;
    }

    public JoinableMessage join(final JoinableMessage part) {
        this.part = part;
        return part;
    }
}
