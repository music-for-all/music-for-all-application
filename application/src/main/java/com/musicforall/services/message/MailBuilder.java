package com.musicforall.services.message;

import org.springframework.mail.SimpleMailMessage;

/**
 * Created by kgavrylchenko on 25.08.16.
 */

public final class MailBuilder {

    private String text;
    private String from;
    private String to;
    private String subject;

    private MailBuilder() {
    }

    public static MailBuilder build() {
        return new MailBuilder();
    }

    public MailBuilder from(String from) {
        this.from = from;
        return this;
    }

    public MailBuilder to(String to) {
        this.to = to;
        return this;
    }

    public MailBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public MailBuilder text(String text) {
        this.text = text;
        return this;
    }

    public SimpleMailMessage get() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
}
