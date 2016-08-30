package com.musicforall.dto.mail;

/**
 * Created by kgavrylchenko on 25.08.16.
 */

public final class Mail {

    private final String text;
    private final String from;
    private final String to;
    private final String subject;

    private Mail(String from, String to, String subject, String text) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public static Builder build() {
        return new Builder();
    }

    public static Builder build(final Mail mail) {
        return new Builder(mail);
    }

    public String getText() {
        return text;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public static final class Builder {
        private String text;
        private String from;
        private String to;
        private String subject;

        private Builder() {
        }

        public Builder(final Mail mail) {
            this.text = mail.text;
            this.from = mail.from;
            this.to = mail.to;
            this.subject = mail.subject;
        }

        public Builder text(String val) {
            text = val;
            return this;
        }

        public Builder from(String val) {
            from = val;
            return this;
        }

        public Builder to(String val) {
            to = val;
            return this;
        }

        public Builder subject(String val) {
            subject = val;
            return this;
        }

        public Mail get() {
            return new Mail(from, to, subject, text);
        }
    }
}
